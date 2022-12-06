package com.faap.scheduler.job_application.file.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.faap.scheduler.job_application.enums.Flag;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.models.thing_to_do.DataListResponse;
import com.faap.scheduler.job_application.models.thing_to_do.FindAllRequestDto;
import com.faap.scheduler.job_application.models.thing_to_do.FindAllResponseDto;
import com.faap.scheduler.job_application.models.thing_to_do.FindRequestDto;
import com.faap.scheduler.job_application.models.thing_to_do.FindResponseDto;
import com.faap.scheduler.job_application.models.thing_to_do.FindType;
import com.faap.scheduler.job_application.models.thing_to_do.JsonParam;
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
	
	public List<DataListResponse> getAllThingToDoList(List<ThingToDoDto> thingToDoDtoList) throws Exception {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
			    .addModule(new JavaTimeModule())
			    .build();
		
		List<JsonParam> jsonParamList = new ArrayList<>();
		for(ThingToDoDto thingToDoDto: thingToDoDtoList) {
			JsonParam jsonParam = new JsonParam();
			jsonParam.setToken(thingToDoDto.getToken());
			jsonParam.setJson(om.writeValueAsString(thingToDoDto));
			
			jsonParamList.add(jsonParam);
		}
		
		FindAllRequestDto findAllRequestDto = new FindAllRequestDto();
		findAllRequestDto.setFindType(FindType.FIND_THING_TO_DO.toString());
		findAllRequestDto.setFlag(Flag.THING_TO_DO.toString());
		findAllRequestDto.setJsonParamList(jsonParamList);
		
		RequestBody payload = RequestBody.create(
				  mediaType,
				  om.writeValueAsString(findAllRequestDto)
					);
		
		Request request = new Request.Builder()
				  .url("http://localhost:8086/secretary-api/thing_to_do/findAll")
				  .method("POST", payload)
				  //.addHeader("X-AUTH-TOKEN", "12a34bcdef5g6789h012ij34567k890123lmn45o67p89q0rs1tuv23wxy456z78")
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		Response response = client.newCall(request).execute();

		FindAllResponseDto findResponseDto = om.readValue(response.body().string(), FindAllResponseDto.class);
		
		return findResponseDto.getResponseList();
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
	
	private List<DataListResponse> getResponseListByAllColumns(List<ThingToDoDto> thingToDoDtoList) throws Exception {

		//Buscando todos los registros en BD (se busca por todas las columnas)
		List<DataListResponse> responseListByAllColumns = this.getAllThingToDoList(thingToDoDtoList);
		
		//Calculando los registros que fueron modificados (es decir, que no hacen match todas sus columnas)
		List<DataListResponse> updatedResponseList = responseListByAllColumns.stream().filter(r -> r.getDataList().size() == 0).collect(Collectors.toList());
		
		//Buscando solo por token para saber si hay que crear un actualizar en BD
		List<ThingToDoDto> updatedThingToDoDtoList = new ArrayList<>();
		for(DataListResponse updatedResponse: updatedResponseList) {
			ThingToDoDto thingToDoDto = new ThingToDoDto();
			thingToDoDto.setToken(updatedResponse.getToken());
			updatedThingToDoDtoList.add(thingToDoDto);
		}
		
		List<DataListResponse> responseListByTokenColumn = this.getAllThingToDoList(updatedThingToDoDtoList);
		
		for(DataListResponse dataListResponse: responseListByAllColumns) {
			
			boolean updated = updatedResponseList.stream().anyMatch(r -> r.getToken().equals(dataListResponse.getToken()));
			//Si la fila fue modificada
			if(updated) {
				//Calculando los registros que son nuevos (es decir, que no hace match la columna token)
				boolean toCreate = responseListByTokenColumn.stream().anyMatch(r -> r.getToken().equals(dataListResponse.getToken()) && r.getDataList().size() == 0);
				if(toCreate) {
					dataListResponse.setSaveType(SaveType.CREATE_THING_TO_DO);
				}
				else {
					List<ThingToDo> thingToDoList = responseListByTokenColumn.stream()
							.filter(r -> r.getToken().equals(dataListResponse.getToken()))
							.findFirst()
							.orElse(null).getDataList();
					dataListResponse.setSaveType(SaveType.UPDATE_THING_TO_DO);
					dataListResponse.setDataList(thingToDoList);
				}
			}
		}
		
		return responseListByAllColumns;
	}
	
	public List<SaveResponseDto> saveThingToDoList(List<RowWrapper> rowWrapperList) throws Exception {
		List<SaveResponseDto> saveResponseDtoList = new ArrayList<>();
		
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		
		ObjectMapper om = JsonMapper.builder()
			    .addModule(new JavaTimeModule())
			    .build();
		
		List<ThingToDoDto> thingToDoDtoList = new ArrayList<>();
		for(RowWrapper rowWrapper: rowWrapperList) {
			
			ThingToDoDto thingToDoDto = new ThingToDoDto(rowWrapper);
			thingToDoDtoList.add(thingToDoDto);
			
		}
		
		List<DataListResponse> responseListByAllColumns = this.getResponseListByAllColumns(thingToDoDtoList);
		
		for(DataListResponse dataListResponse: responseListByAllColumns) {
			if(dataListResponse.getSaveType() != null) {
				
				ThingToDoDto thingToDoDto = thingToDoDtoList.stream().filter(t -> t.getToken().equals(dataListResponse.getToken())).findFirst().orElse(null);
			
				SaveType saveType = SaveType.CREATE_THING_TO_DO;
				
				if(dataListResponse.getSaveType() == SaveType.UPDATE_THING_TO_DO) {
					thingToDoDto.setId(dataListResponse.getDataList().get(0).getId());
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
		}
		
		return saveResponseDtoList;
	}
}
