package com.br.despesa.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.br.despesa.exception.BusinessException;
import com.br.despesa.exception.DadosCadastradosException;
import com.br.despesa.exception.FiltrosInvalidosException;
import com.br.despesa.exception.NaoEncontradoException;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.br.despesa.dto.request.ContaRequest;
import com.br.despesa.dto.request.FiltroRequestExtrato;
import com.br.despesa.dto.response.ContaResponse;
import com.br.despesa.entity.ContaEntity;
import com.br.despesa.entity.MovimentacaoContaEntity;
import com.br.despesa.entity.UsuarioEntity;
import com.br.despesa.enuns.TipoMovimentacaoEnum;
import com.br.despesa.factory.dto.ContaResponseFactory;
import com.br.despesa.factory.entity.ContaEntityFactory;
import com.br.despesa.factory.entity.UsuarioEntityFactory;
import com.br.despesa.repository.ContaRepository;
import com.br.despesa.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContaService {
	
	private final ContaRepository contaRepository;
	
	private final UsuarioRepository usuarioRepository;
	
//	private final MovimentacaoService movimentacaoService;
	private final MovimentacaoService movimentacaoService;
	
	@Transactional
	public void cadastrarConta(ContaRequest contaRequest) {
		UsuarioEntity usuario = extrairUsuario(contaRequest);
		validarNumeroConta(contaRequest);
		extrairConta(contaRequest, usuario);
	}

	private void extrairConta(ContaRequest contaRequest, UsuarioEntity usuario) {
		ContaEntity conta = ContaEntityFactory.converterParaEntity(contaRequest, usuario);
		contaRepository.save(conta);
	}

	private void validarNumeroConta(ContaRequest contaRequest) {
		if(contaRepository.existsByNumeroConta(contaRequest.numeroConta()))
			throw new DadosCadastradosException("Já existe um cadastro de uma conta com esse número");
	}

	private UsuarioEntity extrairUsuario(ContaRequest contaRequest) {
		if(usuarioRepository.existsByUsuario(contaRequest.usuario()))
			throw new DadosCadastradosException("Usuário já cadastrado");
		
		UsuarioEntity usuario = UsuarioEntityFactory.converterParaEntity(contaRequest);
		return usuarioRepository.save(usuario);
	}
	
	public ContaResponse buscarContaPorId(Long id) {
		ContaEntity conta = buscarConta(id);
		return ContaResponseFactory.converterReponse(conta);
	}
	
	private ContaEntity buscarConta(Long idConta) {
		return contaRepository.findById(idConta).orElseThrow(() -> new NaoEncontradoException("Nenhuma conta encontrada"));
	}

	public Resource gerarXlsDespesas(Long id, LocalDate dataInicio, LocalDate dataFim) {

		if(Objects.isNull(dataInicio) || Objects.isNull(dataFim))
			throw new FiltrosInvalidosException("Informe uma data");

		List<MovimentacaoContaEntity> movimentacoes = movimentacaoService.pesquisarExtratoMovimentacoesEntity(new FiltroRequestExtrato(id, dataInicio, dataFim));
		validarTamanhoExtrato(movimentacoes);
		
		Resource r = null;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Despesas");

		// Definindo alguns padroes de layout
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeight((short)400);

		int rownum = 0;
		int cellnum = 0;

		//Configurando estilos de células (Cores, alinhamento, formatação, etc..)
		HSSFDataFormat numberFormat = workbook.createDataFormat();

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

		CellStyle textStyle = workbook.createCellStyle();

		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));

		rownum = configurarHeader(sheet, rownum, cellnum, headerStyle);
		extrairDadosParaPlanilha(movimentacoes, sheet, rownum, textStyle, numberStyle);

		return gerarXls(r, workbook);

	}

	private void validarTamanhoExtrato(List<MovimentacaoContaEntity> movimentacoes) {
		if(movimentacoes.isEmpty()) {
			throw new NaoEncontradoException("Não há registros disponíveis");
		}
		
		if(!movimentacoes.isEmpty() && movimentacoes.size() > 1000) {
			throw new BusinessException("Foram encontrados mais de 1000 registros. Refine sua busca");
		}
	}

	private void extrairDadosParaPlanilha(List<MovimentacaoContaEntity> movimentacoes, HSSFSheet sheet, int rownum, CellStyle textStyle,
			CellStyle numberStyle) {
		
		int cellnum;
		Cell cell;
		Row row;
		
		double totalSaida = 0.0d;
		double totalEntrada = 0.0d;
		
		for (MovimentacaoContaEntity movimentacao : movimentacoes) {
			row = sheet.createRow(rownum++);
			cellnum = 0;

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(movimentacao.getId());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(movimentacao.getDescricao());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(movimentacao.getValorMovimentado().doubleValue());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(movimentacao.getTipoMovimentacao().getDescricao());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(textStyle);
			cell.setCellValue(Objects.nonNull(movimentacao.getTipoDespesa()) ? movimentacao.getTipoDespesa().getDescricao() : "");

			cell = row.createCell(cellnum++);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(formatarData(movimentacao.getDataMovimentacao()));

			if(Objects.equals(TipoMovimentacaoEnum.MOV_SAIDA, movimentacao.getTipoMovimentacao())) {
				totalSaida -= movimentacao.getValorMovimentado().doubleValue();
			} else {
				totalEntrada += movimentacao.getValorMovimentado().doubleValue();
			}
		}
		
		// Total saída - Adicionando na primeira linha, coluna 'Total gasto'
	    row = sheet.getRow(1);
	    if (row == null) {
	        row = sheet.createRow(1);
	    }
	    cell = row.createCell(6);
	    cell.setCellStyle(numberStyle);
	    cell.setCellValue(BigDecimal.valueOf(totalSaida).negate().doubleValue());
	    
	    // Total entrada - Adicionando na primeira linha, coluna 'Valor restante'
	    cell = row.createCell(7);
	    cell.setCellStyle(numberStyle);
	    cell.setCellValue(totalEntrada);

	    ContaEntity conta = Optional.ofNullable(movimentacoes)
	    	    .filter(movs -> !movs.isEmpty())
	    	    .map(movs -> movs.get(0).getConta())
	    	    .orElseThrow(() -> new NaoEncontradoException("Nenhuma movimentação encontrada para extrair a conta."));
	    
	    // Saldo - Adicionando na primeira linha, coluna 'Valor restante'
	    cell = row.createCell(8);
	    cell.setCellStyle(numberStyle);
	    cell.setCellValue(conta.getSaldo().doubleValue());
		
		
	}

	private Resource gerarXls(Resource r, HSSFWorkbook workbook) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			workbook.write(out);
			workbook.close(); 
			byte[] bytes = out.toByteArray();
			r = new ByteArrayResource(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null; 
		}
		
		return r;
	}

	private int configurarHeader(HSSFSheet sheet, int rownum, int cellnum, CellStyle headerStyle) {
		Cell cell;
		Row row;
		
		// Configurando Header
		row = sheet.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Id movimentação");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Descrição");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Valor gasto");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Tipo movimentação");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Tipo despesa");

		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Data");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Total de saída");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Total de entrada");
		
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Valor restante");
		
		return rownum;
	}
	
	private String formatarData(ZonedDateTime data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
	}

}
