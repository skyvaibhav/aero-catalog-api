package com.hc.jsonbean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonHighscore {

	@JsonProperty("playerName")
	private String _playerName;
	
	@JsonProperty("playerScore")
	private Double _playerScore;
	
	@JsonProperty("questionSetID")
	private Long _questionSetID;

	public JsonHighscore() {
		super();
	}

	public JsonHighscore(String _playerName, Double _playerScore, Long _questionSetID) {
		super();
		this._playerName = _playerName;
		this._playerScore = _playerScore;
		this._questionSetID = _questionSetID;
	}

	public String get_playerName() {
		return _playerName;
	}

	public void set_playerName(String _playerName) {
		this._playerName = _playerName;
	}

	public Double get_playerScore() {
		return _playerScore;
	}

	public void set_playerScore(Double _playerScore) {
		this._playerScore = _playerScore;
	}

	public Long get_questionSetID() {
		return _questionSetID;
	}

	public void set_questionSetID(Long _questionSetID) {
		this._questionSetID = _questionSetID;
	}

	@Override
	public String toString() {
		return "JsonHighscore [_playerName=" + _playerName + ", _playerScore=" + _playerScore + ", _questionSetID="
				+ _questionSetID + "]";
	}

}
