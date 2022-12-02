package com.faap.scheduler.job_application.file.services;

import java.util.List;

import com.faap.scheduler.job_application.models.thing_to_do.FindRequestDto;
import com.faap.scheduler.job_application.models.thing_to_do.FindResponseDto;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDo;
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

	public FindResponseDto saveThingToDoList() throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		FindRequestDto findRequestDto = new FindRequestDto();
		findRequestDto.setFindType("FIND_THING_TO_DO");
		findRequestDto.setFlag("THING_TO_DO");
		findRequestDto.setJsonParams("{ \"status\": \"PENDING\" }");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(findRequestDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8084/log-api/dataLog/find")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		//Gson gson = new Gson(); 
		FindResponseDto findResponseDto = om.readValue(response.body().string(), FindResponseDto.class);
		
		return findResponseDto;
	}
	
	
	public List<ThingToDo> getThingToDoList() throws Exception {
		OkHttpClient client = new OkHttpClient();
		
		FindRequestDto findRequestDto = new FindRequestDto();
		findRequestDto.setFindType("FIND_THING_TO_DO");
		findRequestDto.setFlag("THING_TO_DO");
		findRequestDto.setJsonParams("{ \"status\": \"PENDING\" }");
		
		ObjectMapper om = JsonMapper.builder()
	    .addModule(new JavaTimeModule())
	    .build();
		
		Request request = new Request.Builder()
				  .url("http://localhost:8086/secretary-api/thing_to_do/REACT")
	
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		//Gson gson = new Gson(); 
		FindResponseDto findResponseDto = om.readValue(response.body().string(), FindResponseDto.class);
		
		return findResponseDto.getDataList();
	}
}
