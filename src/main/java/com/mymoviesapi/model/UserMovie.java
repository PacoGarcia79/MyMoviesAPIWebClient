package com.mymoviesapi.model;

import lombok.Data;

@Data
public class UserMovie {

	private int userid;
	private int movieid;
	private boolean favourite;
	private int personal_rating;
	private String notes;
}
