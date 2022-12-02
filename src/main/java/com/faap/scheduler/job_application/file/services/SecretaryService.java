package com.faap.scheduler.job_application.file.services;

import java.util.ArrayList;
import java.util.List;

import com.faap.scheduler.job_application.enums.Flag;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.models.thing_to_do.FindRequestDto;
import com.faap.scheduler.job_application.models.thing_to_do.FindResponseDto;
import com.faap.scheduler.job_application.models.thing_to_do.FindType;
import com.faap.scheduler.job_application.models.thing_to_do.SaveRequestDto;
import com.faap.scheduler.job_application.models.thing_to_do.SaveResponseDto;
import com.faap.scheduler.job_application.models.thing_to_do.SaveType;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDo;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class SecretaryService {
	
	public SecretaryService() {}	
	
	public List<ThingToDo> getThingToDoList() throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8086/secretary-api/thing_to_do/status/REACT")
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindResponseDto findResponseDto = om.readValue(response.body().string(), FindResponseDto.class);
		
		return findResponseDto.getDataList();
	}
	
	public List<ThingToDo> getThingToDoList(ThingToDoDto thingToDoDto) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
			    .addModule(new JavaTimeModule())
			    .build();
		
		FindRequestDto findRequestDto = new FindRequestDto();
		findRequestDto.setFindType(FindType.FIND_THING_TO_DO.toString());
		findRequestDto.setFlag(Flag.THING_TO_DO.toString());
		findRequestDto.setJsonParams(om.writeValueAsString(thingToDoDto));
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(findRequestDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8086/secretary-api/thing_to_do/find")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindResponseDto findResponseDto = om.readValue(response.body().string(), FindResponseDto.class);
		
		return findResponseDto.getDataList();
	}
	
	public List<SaveResponseDto> saveThingToDoList(List<RowWrapper> rowWrapperList) throws Exception {
		List<SaveResponseDto> saveResponseDtoList = new ArrayList<>();
		
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
			    .addModule(new JavaTimeModule())
			    .build();
		
		for(RowWrapper rowWrapper: rowWrapperList) {
			
			ThingToDoDto thingToDoDto = new ThingToDoDto(rowWrapper);
			SaveType saveType = SaveType.CREATE_THING_TO_DO;
			
			List<ThingToDo> thingToDoList = this.getThingToDoList(thingToDoDto);
			if(thingToDoList.size() > 0) {
				thingToDoDto.setId(thingToDoList.get(0).getId());
				saveType = SaveType.UPDATE_THING_TO_DO;
			}
			
			SaveRequestDto saveRequestDto = new SaveRequestDto();
			saveRequestDto.setSaveType(saveType.toString());
			saveRequestDto.setFlag(Flag.THING_TO_DO.toString());
			saveRequestDto.setJsonParams(om.writeValueAsString(thingToDoDto));
			
			RequestBody payload = RequestBody.create(
					  mediaType,
					  om.writeValueAsString(saveRequestDto)
						);
			
			Request request = new Request.Builder()
					  .url("http://localhost:8086/secretary-api/thing_to_do/save")
					  .method("POST", payload)
					  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
					  .addHeader("Content-Type", "application/json")
					  .build();
			
			Response response = client.newCall(request).execute();
	 
			SaveResponseDto saveResponseDto = om.readValue(response.body().string(), SaveResponseDto.class);
			
			saveResponseDtoList.add(saveResponseDto);
		}
		
		return saveResponseDtoList;
	}
}
