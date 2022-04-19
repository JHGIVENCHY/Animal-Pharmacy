package com.ap.consumer.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Head {
	private int list_total_count;
	private List<Result> RESULT;
	private String api_version;
}
