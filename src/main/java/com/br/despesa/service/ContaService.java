package com.br.despesa.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
import com.br.despesa.dto.response.ContaResponse;
import com.br.despesa.entity.ContaEntity;
import com.br.despesa.entity.MovimentacaoContaEntity;
import com.br.despesa.enuns.TipoMovimentacaoEnum;
import com.br.despesa.factory.ContaResponseFactory;
import com.br.despesa.repository.ContaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContaService {
	
	private final ContaRepository contaRepository;
	
	@Transactional
	public void cadastrarConta(ContaRequest contaRequest) {
		
		if(contaRepository.existsByNumeroConta(contaRequest.numeroConta()))
			throw new RuntimeException("Já existe um cadastro de uma conta com esse número");
		
		ContaEntity conta = ContaEntity.builder()
		.nomeConta(contaRequest.nomeConta())
		.numeroConta(contaRequest.numeroConta())
		.build();
		
		contaRepository.save(conta);
		
	}
	
	public ContaResponse buscarContaPorId(Long id) {
		ContaEntity conta = buscarConta(id);
		return ContaResponseFactory.converterReponse(conta);
	}
	
	private ContaEntity buscarConta(Long idConta) {
		return contaRepository.findById(idConta).orElseThrow(() -> new RuntimeException("Nenhuma conta encontrada"));
	}

	public Resource gerarXlsDespesas(Long id) {

		ContaEntity conta = buscarConta(id);

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
		extrairDadosParaPlanilha(conta, sheet, rownum, textStyle, numberStyle);

		return gerarXls(r, workbook);

	}

	private void extrairDadosParaPlanilha(ContaEntity conta, HSSFSheet sheet, int rownum, CellStyle textStyle,
			CellStyle numberStyle) {
		
		int cellnum;
		Cell cell;
		Row row;
		
		double totalSaida = 0.0d;
		double totalEntrada = 0.0d;
		
		for (MovimentacaoContaEntity movimentacao : conta.getMovimentacoes()) {
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
