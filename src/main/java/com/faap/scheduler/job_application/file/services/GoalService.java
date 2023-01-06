package com.faap.scheduler.job_application.file.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.faap.scheduler.job_application.excel.models.DoneColumnType;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.done.CreateDoneResponse;
import com.faap.scheduler.job_application.excel.models.done.Discipline;
import com.faap.scheduler.job_application.excel.models.done.DisciplineDto;
import com.faap.scheduler.job_application.excel.models.done.Done;
import com.faap.scheduler.job_application.excel.models.done.DoneDto;
import com.faap.scheduler.job_application.excel.models.done.DoneTemplate;
import com.faap.scheduler.job_application.excel.models.done.DoneTemplateDto;
import com.faap.scheduler.job_application.excel.models.done.Unit;
import com.faap.scheduler.job_application.excel.models.done.UnitDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class GoalService {
	
	public DoneTemplate findDoneTemplate(String name, Integer customerId) {
		return null;
	}
	
	public DoneTemplate createDoneTemplate(DoneTemplateDto doneTemplateDto) {
		return null;
	}
	
	public Discipline findDiscipline(String name, Integer customerId) {
		return null;
	}
	
	public Discipline createDiscipline(DisciplineDto disciplineDto) {
		return null;
	}
	
	public Unit findUnit(String name, Integer customerId) {
		return null;
	}
	
	public Unit createUnit(UnitDto unitDto) {
		return null;
	}
	
	public Done findDone(DoneDto doneDto) {
		return null;
	}

	public List<Done> saveDoneList(List<RowWrapper> rowWrapperList) throws Exception {
		List<Done> doneList = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();

		
		for(RowWrapper rowWrapper: rowWrapperList) {
			LocalDate localDate = this.parseStrToLocalDate(rowWrapper.getCellWrapperList().get(DoneColumnType.DATE.getColumnIndex()).getCellValue());
			String formatStrDate = this.getStringFromDate(localDate);
			Integer initialHour = Integer.parseInt(rowWrapper.getCellWrapperList().get(DoneColumnType.INITIAL_HOUR.getColumnIndex()).getCellValue());
			Integer initialMinute = Integer.parseInt(rowWrapper.getCellWrapperList().get(DoneColumnType.INITIAL_MINUTE.getColumnIndex()).getCellValue());
			
			Integer finalHour = Integer.parseInt(rowWrapper.getCellWrapperList().get(DoneColumnType.FINAL_HOUR.getColumnIndex()).getCellValue());
			Integer finalMinute = Integer.parseInt(rowWrapper.getCellWrapperList().get(DoneColumnType.FINAL_MINUTE.getColumnIndex()).getCellValue());
			
			String description = rowWrapper.getCellWrapperList().get(DoneColumnType.DESCRIPTION.getColumnIndex()).getCellValue();
			String discipline = rowWrapper.getCellWrapperList().get(DoneColumnType.DISCIPLINE.getColumnIndex()).getCellValue();
			
			Double result = Double.parseDouble(rowWrapper.getCellWrapperList().get(DoneColumnType.RESULT.getColumnIndex()).getCellValue());
			String unit = rowWrapper.getCellWrapperList().get(DoneColumnType.UNIT.getColumnIndex()).getCellValue();
			
			DoneDto doneDto = new DoneDto();
			doneDto.setDate(formatStrDate);
			doneDto.setInitialHour(initialHour);
			doneDto.setInitialMinute(initialMinute);
			doneDto.setFinalHour(finalHour);
			doneDto.setFinalMinute(finalMinute);
			doneDto.setDescription(description);
			doneDto.setDiscipline(discipline);
			doneDto.setResult(result);
			doneDto.setUnit(unit);
			

				
			RequestBody payload = RequestBody.create(
					  mediaType,
					  om.writeValueAsString(doneDto)
						);
			
			Request request = new Request.Builder()
					  .url("http://localhost:8087/goal-api/done/create")
					  .method("POST", payload)
					  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
					  .addHeader("Content-Type", "application/json")
					  .build();
			
			Response response = client.newCall(request).execute();
	 
			CreateDoneResponse createDoneResponse = om.readValue(response.body().string(), CreateDoneResponse.class);
			
			doneList.add(createDoneResponse.getPayload());	
		}
		
		return doneList;
	}
	
	public LocalDate parseStrToLocalDate(String strDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		return (strDate == null || "".equals(strDate) ? null : LocalDate.parse(strDate, formatter));
	}
	
	public String getStringFromDate(LocalDate date) {
		String dateFormat = "yyyyMMdd";
		if(date == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		return date.format(formatter);

	}
}
