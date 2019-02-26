package com.hc.db.entity;

public class Highscore {

	private Long high_score_id;
	
	private Long user_id;
	
	private String player_name;
	
	private String player_score;
	
	private Long category_id;
	
	public Highscore() {
		super();
	}

	public Highscore(Long user_id, String player_name, String player_score, Long category_id) {
		super();
		this.user_id = user_id;
		this.player_name = player_name;
		this.player_score = player_score;
		this.category_id = category_id;
	}

	public Highscore(Long high_score_id, Long user_id, String player_name, String player_score, Long category_id) {
		super();
		this.high_score_id = high_score_id;
		this.user_id = user_id;
		this.player_name = player_name;
		this.player_score = player_score;
		this.category_id = category_id;
	}

	public Long getHigh_score_id() {
		return high_score_id;
	}

	public void setHigh_score_id(Long high_score_id) {
		this.high_score_id = high_score_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public String getPlayer_score() {
		return player_score;
	}

	public void setPlayer_score(String player_score) {
		this.player_score = player_score;
	}

	public Long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}

	@Override
	public String toString() {
		return "Highscore [high_score_id=" + high_score_id + ", user_id=" + user_id + ", player_name=" + player_name
				+ ", player_score=" + player_score + ", category_id=" + category_id + "]";
	}

}
