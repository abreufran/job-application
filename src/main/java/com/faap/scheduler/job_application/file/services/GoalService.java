package com.faap.scheduler.job_application.file.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.faap.scheduler.job_application.excel.models.DoneColumnType;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.done.CreateDailyCategoryResponse;
import com.faap.scheduler.job_application.excel.models.done.CreateDisciplineResponse;
import com.faap.scheduler.job_application.excel.models.done.CreateDoneResponse;
import com.faap.scheduler.job_application.excel.models.done.CreateDoneTemplateResponse;
import com.faap.scheduler.job_application.excel.models.done.CreatePeriodicResponse;
import com.faap.scheduler.job_application.excel.models.done.CreatePeriodicityResponse;
import com.faap.scheduler.job_application.excel.models.done.CreatePriorityResponse;
import com.faap.scheduler.job_application.excel.models.done.CreateUnitResponse;
import com.faap.scheduler.job_application.excel.models.done.CreateWeekdayResponse;
import com.faap.scheduler.job_application.excel.models.done.DailyCategory;
import com.faap.scheduler.job_application.excel.models.done.DailyCategoryDto;
import com.faap.scheduler.job_application.excel.models.done.Discipline;
import com.faap.scheduler.job_application.excel.models.done.DisciplineDto;
import com.faap.scheduler.job_application.excel.models.done.Done;
import com.faap.scheduler.job_application.excel.models.done.DoneDto;
import com.faap.scheduler.job_application.excel.models.done.DoneTemplate;
import com.faap.scheduler.job_application.excel.models.done.DoneTemplateDto;
import com.faap.scheduler.job_application.excel.models.done.FindDailyCategoryResponse;
import com.faap.scheduler.job_application.excel.models.done.FindDisciplineResponse;
import com.faap.scheduler.job_application.excel.models.done.FindDoneResponse;
import com.faap.scheduler.job_application.excel.models.done.FindDoneTemplateResponse;
import com.faap.scheduler.job_application.excel.models.done.FindPeriodicResponse;
import com.faap.scheduler.job_application.excel.models.done.FindPeriodicityResponse;
import com.faap.scheduler.job_application.excel.models.done.FindPriorityResponse;
import com.faap.scheduler.job_application.excel.models.done.FindUnitResponse;
import com.faap.scheduler.job_application.excel.models.done.FindWeekdayResponse;
import com.faap.scheduler.job_application.excel.models.done.Periodic;
import com.faap.scheduler.job_application.excel.models.done.PeriodicDto;
import com.faap.scheduler.job_application.excel.models.done.Periodicity;
import com.faap.scheduler.job_application.excel.models.done.PeriodicityDto;
import com.faap.scheduler.job_application.excel.models.done.Priority;
import com.faap.scheduler.job_application.excel.models.done.PriorityDto;
import com.faap.scheduler.job_application.excel.models.done.Unit;
import com.faap.scheduler.job_application.excel.models.done.UnitDto;
import com.faap.scheduler.job_application.excel.models.done.Weekday;
import com.faap.scheduler.job_application.excel.models.done.WeekdayDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class GoalService {
	
	public DoneTemplate findDoneTemplate(String name, Integer customerId) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/done_template/name/" + name + "/customer_id/" + customerId)
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindDoneTemplateResponse findDoneTemplateResponse = om.readValue(response.body().string(), FindDoneTemplateResponse.class);
		
		if(findDoneTemplateResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findDoneTemplateResponse.getPayload().get(0);
        }
	}
	
	public DoneTemplate createDoneTemplate(DoneTemplateDto doneTemplateDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(doneTemplateDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/done_template/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreateDoneTemplateResponse createDoneTemplateResponse = om.readValue(response.body().string(), CreateDoneTemplateResponse.class);
		
		return createDoneTemplateResponse.getPayload();
	}
	
	public Discipline findDiscipline(String name, Integer customerId) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/discipline/name/" + name + "/customer_id/" + customerId)
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindDisciplineResponse findDisciplineResponse = om.readValue(response.body().string(), FindDisciplineResponse.class);
		
		if(findDisciplineResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findDisciplineResponse.getPayload().get(0);
        }
	}
	
	public Discipline createDiscipline(DisciplineDto disciplineDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(disciplineDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/discipline/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreateDisciplineResponse createDisciplineResponse = om.readValue(response.body().string(), CreateDisciplineResponse.class);
		
		return createDisciplineResponse.getPayload();
	}
	
	public Unit findUnit(String name, Integer customerId) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/unit/name/" + name + "/customer_id/" + customerId)
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindUnitResponse findUnitResponse = om.readValue(response.body().string(), FindUnitResponse.class);
		
		if(findUnitResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findUnitResponse.getPayload().get(0);
        }
	}
	
	public Unit createUnit(UnitDto unitDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(unitDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/unit/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreateUnitResponse createUnitResponse = om.readValue(response.body().string(), CreateUnitResponse.class);
		
		return createUnitResponse.getPayload();
	}
	
	public Done findDone(DoneDto doneDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(doneDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/done/find")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindDoneResponse findDoneResponse = om.readValue(response.body().string(), FindDoneResponse.class);
		
		if(findDoneResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findDoneResponse.getPayload().get(0);
        }
	}
	
	public Done createDone(DoneDto doneDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
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
		
		return createDoneResponse.getPayload();
	}
	
	public Periodicity findPeriodicity(String name, Integer customerId) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/periodicity/name/" + name + "/customer_id/" + customerId)
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindPeriodicityResponse findPeriodicityResponse = om.readValue(response.body().string(), FindPeriodicityResponse.class);
		
		if(findPeriodicityResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findPeriodicityResponse.getPayload().get(0);
        }
	}
	
	public Periodicity createPeriodicity(PeriodicityDto periodicityDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(periodicityDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/periodicity/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreatePeriodicityResponse createPeriodicityResponse = om.readValue(response.body().string(), CreatePeriodicityResponse.class);
		
		return createPeriodicityResponse.getPayload();
	}
	
	public Weekday findWeekday(String name, Integer customerId) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/weekday/name/" + name + "/customer_id/" + customerId)
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindWeekdayResponse findWeekdayResponse = om.readValue(response.body().string(), FindWeekdayResponse.class);
		
		if(findWeekdayResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findWeekdayResponse.getPayload().get(0);
        }
	}
	
	public Weekday createWeekday(WeekdayDto weekdayDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(weekdayDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/weekday/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreateWeekdayResponse createWeekdayResponse = om.readValue(response.body().string(), CreateWeekdayResponse.class);
		
		return createWeekdayResponse.getPayload();
	}
	
	
	public Priority findPriority(String name, Integer customerId) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/priority/name/" + name + "/customer_id/" + customerId)
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindPriorityResponse findPriorityResponse = om.readValue(response.body().string(), FindPriorityResponse.class);
		
		if(findPriorityResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findPriorityResponse.getPayload().get(0);
        }
	}
	
	public Priority createPriority(PriorityDto priorityDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(priorityDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/priority/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreatePriorityResponse createPriorityResponse = om.readValue(response.body().string(), CreatePriorityResponse.class);
		
		return createPriorityResponse.getPayload();
	}
	
	public DailyCategory findDailyCategory(String name, Integer customerId) throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/daily_category/name/" + name + "/customer_id/" + customerId)
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindDailyCategoryResponse findDailyCategoryResponse = om.readValue(response.body().string(), FindDailyCategoryResponse.class);
		
		if(findDailyCategoryResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findDailyCategoryResponse.getPayload().get(0);
        }
	}
	
	public DailyCategory createDailyCategory(DailyCategoryDto dailyCategoryDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(dailyCategoryDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/daily_category/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreateDailyCategoryResponse createDailyCategoryResponse = om.readValue(response.body().string(), CreateDailyCategoryResponse.class);
		
		return createDailyCategoryResponse.getPayload();
	}
	
	public Periodic findPeriodic(PeriodicDto periodicDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(periodicDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/periodic/find")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindPeriodicResponse findPeriodicResponse = om.readValue(response.body().string(), FindPeriodicResponse.class);
		
		if(findPeriodicResponse.getPayload().size() == 0) {
        	return null;
        }
        else {
        	return findPeriodicResponse.getPayload().get(0);
        }
	}
	
	public Periodic createPeriodic(PeriodicDto periodicDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(periodicDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8087/goal-api/periodic/create")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		CreatePeriodicResponse createPeriodicResponse = om.readValue(response.body().string(), CreatePeriodicResponse.class);
		
		return createPeriodicResponse.getPayload();
	}

	public List<Done> exportDoneRowWrapperList(List<RowWrapper> rowWrapperList, 
			int customerId, String channelName) throws Exception {
		List<Done> doneList = new ArrayList<>();
		

		int rowNumber = 0;
		for(RowWrapper rowWrapper: rowWrapperList) {
			rowNumber++;
			try {
				LocalDate localDate = this.parseStrToLocalDate(rowWrapper.getCellWrapperList().get(DoneColumnType.DATE.getColumnIndex()).getCellValue());
				String formatStrDate = this.getStringFromDate(localDate);
				Integer initialHour = (int) Double.parseDouble(rowWrapper.getCellWrapperList().get(DoneColumnType.INITIAL_HOUR.getColumnIndex()).getCellValue());
				Integer initialMinute = (int) Double.parseDouble(rowWrapper.getCellWrapperList().get(DoneColumnType.INITIAL_MINUTE.getColumnIndex()).getCellValue());
				
				Integer finalHour = (int) Double.parseDouble(rowWrapper.getCellWrapperList().get(DoneColumnType.FINAL_HOUR.getColumnIndex()).getCellValue());
				Integer finalMinute = (int) Double.parseDouble(rowWrapper.getCellWrapperList().get(DoneColumnType.FINAL_MINUTE.getColumnIndex()).getCellValue());
				
				String description = rowWrapper.getCellWrapperList().get(DoneColumnType.DESCRIPTION.getColumnIndex()).getCellValue();
				String disciplineName = rowWrapper.getCellWrapperList().get(DoneColumnType.DISCIPLINE.getColumnIndex()).getCellValue();
				
				String resultCell = rowWrapper.getCellWrapperList().get(DoneColumnType.RESULT.getColumnIndex()).getCellValue();
				Double result = null;
				if(resultCell != null) {
					result = Double.parseDouble(rowWrapper.getCellWrapperList().get(DoneColumnType.RESULT.getColumnIndex()).getCellValue());
				}
				String unitName = rowWrapper.getCellWrapperList().get(DoneColumnType.UNIT.getColumnIndex()).getCellValue();
				
				Discipline discipline = findDiscipline(disciplineName, customerId);
				
				if(discipline == null) {
					DisciplineDto dto = new DisciplineDto();
					dto.setName(disciplineName);
					dto.setDescription("");
					dto.setCustomerId(customerId);
					dto.setChannel(channelName);
					
					discipline = createDiscipline(dto);
				}
				
				Unit unit = null;
				
				if(unitName != null) {
					unit = findUnit(unitName, customerId);
					
					if(unit == null) {
						UnitDto dto = new UnitDto();
						dto.setName(unitName);
						dto.setDescription("");
						dto.setCustomerId(customerId);
						dto.setChannel(channelName);
						
						unit = createUnit(dto);
					}
				}
				
				DoneTemplate doneTemplate = findDoneTemplate(description, customerId);
				
				if(doneTemplate == null) {
					DoneTemplateDto dto = new DoneTemplateDto();
					dto.setDefaultInitialHour(initialHour);
					dto.setDefaultInitialMinute(initialMinute);
					dto.setDefaultFinalHour(finalHour);
					dto.setDefaultFinalMinute(finalMinute);
					dto.setDescription(description);
					dto.setDefaultDiscipline(discipline.getName());
					dto.setDefaultResult(result);
					dto.setDefaultUnit(unitName);
					dto.setCustomerId(customerId);
					dto.setChannel(channelName);
					
					doneTemplate = createDoneTemplate(dto);
				}
				
				DoneDto doneDto = new DoneDto();
				doneDto.setDate(formatStrDate);
				doneDto.setInitialHour(initialHour);
				doneDto.setInitialMinute(initialMinute);
				doneDto.setFinalHour(finalHour);
				doneDto.setFinalMinute(finalMinute);
				doneDto.setDescription(description);
				doneDto.setDiscipline(disciplineName);
				doneDto.setResult(result);
				doneDto.setUnit(unitName);
				doneDto.setCustomerId(customerId);
				doneDto.setChannel(channelName);
				
				Done done = findDone(doneDto);
				
				if(done == null) {
					done = createDone(doneDto);
					System.out.println("Created Done Task: " + done);
				}
				else {
					System.out.println("Exist Done Task: " + done);
				}
				
				doneList.add(done);	
			}
			catch (Exception e) {
				System.out.println("Invalid Row: " + (rowNumber + 1) + ". Incomplete fields." + e);
			}
		}
		
		return doneList;
	}
	
	public List<Periodic> exportPeriodicRowWrapperList(List<RowWrapper> rowWrapperList, 
			int customerId, String channelName) throws Exception {
		List<Periodic> periodicList = new ArrayList<>();
		

		int rowNumber = 0;
		for(RowWrapper rowWrapper: rowWrapperList) {
			rowNumber++;
			try {
				LocalDate initialDate = this.parseStrToLocalDate(rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.INITIAL_DATE.getColumnIndex()).getCellValue());
				String formatInitialStrDate = this.getStringFromDate(initialDate);
				LocalDate finalDate = this.parseStrToLocalDate(rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.FINAL_DATE.getColumnIndex()).getCellValue());
				String formatFinalStrDate = this.getStringFromDate(finalDate);

				String description = rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.TASK.getColumnIndex()).getCellValue();
				String priorityName = rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.PRIORITY.getColumnIndex()).getCellValue();
				
				String dailyCategoryName = rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.CATEGORY.getColumnIndex()).getCellValue();
				String periodicityName = rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.PERIODICITY.getColumnIndex()).getCellValue();
				String weekdayName = rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.WEEKDAY.getColumnIndex()).getCellValue();
				String statusName = rowWrapper.getCellWrapperList().get(PeriodicTaskColumnType.STATUS.getColumnIndex()).getCellValue();
				
				
				
				Priority priority = findPriority(priorityName, customerId);
				
				if(priority == null) {
					PriorityDto dto = new PriorityDto();
					dto.setName(priorityName);
					dto.setDescription("");
					dto.setCustomerId(customerId);
					dto.setChannel(channelName);
					
					priority = createPriority(dto);
				}
				
				DailyCategory dailyCategory = null;
				
				if(dailyCategoryName != null) {
					dailyCategory = findDailyCategory(dailyCategoryName, customerId);
					
					if(dailyCategory == null) {
						DailyCategoryDto dto = new DailyCategoryDto();
						dto.setName(dailyCategoryName);
						dto.setDescription("");
						dto.setCustomerId(customerId);
						dto.setChannel(channelName);
						
						dailyCategory = createDailyCategory(dto);
					}
				}
				
				Periodicity periodicity = null;
				
				if(periodicityName != null) {
					periodicity = findPeriodicity(periodicityName, customerId);
					
					if(periodicity == null) {
						Integer size  = com.faap.scheduler.job_application.excel.models.Periodicity.getPeriodicity(periodicityName).getSize();
						Integer dayNumber = com.faap.scheduler.job_application.excel.models.Periodicity.getPeriodicity(periodicityName).getMonthDayNumber();
						
						PeriodicityDto dto = new PeriodicityDto();
						dto.setName(periodicityName);
						dto.setDescription("");
						dto.setSize((size != -1 ? size : null));
						dto.setDayNumber((dayNumber != -1 ? dayNumber : null));
						dto.setCustomerId(customerId);
						dto.setChannel(channelName);
						
						periodicity = createPeriodicity(dto);
					}
				}
				
				
				Weekday weekday = null;
				
				if(weekdayName != null) {
					weekday = findWeekday(weekdayName, customerId);
					
					if(weekday == null) {
						WeekdayDto dto = new WeekdayDto();
						dto.setName(weekdayName);
						dto.setDescription("");
						dto.setCustomerId(customerId);
						dto.setChannel(channelName);
						
						weekday = createWeekday(dto);
					}
				}
				
				PeriodicDto periodicDto = new PeriodicDto();
				periodicDto.setInitialDate(formatInitialStrDate);
				periodicDto.setFinalDate(formatFinalStrDate);
				
				
				periodicDto.setDescription(description);
				
				periodicDto.setPriority(priorityName);
				
				periodicDto.setDailyCategory(dailyCategoryName);
				
				periodicDto.setPeriodicity(periodicityName);
				
				periodicDto.setWeekday(weekdayName);
				
				periodicDto.setStatus(statusName);
				
				periodicDto.setCustomerId(customerId);
				periodicDto.setChannel(channelName);
				
				Periodic periodic = findPeriodic(periodicDto);
				
				if(periodic == null) {
					periodic = createPeriodic(periodicDto);
					System.out.println("Created Periodic Task: " + periodic);
				}
				else {
					System.out.println("Exist Periodic Task: " + periodic);
				}
				
				periodicList.add(periodic);	
			}
			catch (Exception e) {
				System.out.println("Invalid Row: " + (rowNumber + 1) + ". Incomplete fields." + e);
			}
		}
		
		return periodicList;
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
