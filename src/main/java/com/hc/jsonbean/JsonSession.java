package com.hc.jsonbean;

import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonSession {

	@JsonProperty("sessionId")
	private Long _sessionId;
	
	@JsonProperty("userId")
	private Long _userId;
	
	@JsonProperty("questionSetId")
	private Long _questionSetId;
	
	@JsonProperty("correctAnswers")
	private int _correctAnswers;
	
	@JsonProperty("wrongAnswers")
	private int _wrongAnswers;
	
	@JsonProperty("lastAccessDate")
	private Date _lastAccessDate;
	
	@JsonProperty("timeLeft")
	private long _timeLeft;
	
	@JsonProperty("sessionDetails")
	private JsonSessionDetails _sessionDetails[];

	public JsonSession() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public JsonSession(Long _sessionId, Long _userId, Long _questionSetId, int _correctAnswers, int _wrongAnswers,
			Date _lastAccessDate, long _timeLeft, JsonSessionDetails[] _sessionDetails) {
		super();
		this._sessionId = _sessionId;
		this._userId = _userId;
		this._questionSetId = _questionSetId;
		this._correctAnswers = _correctAnswers;
		this._wrongAnswers = _wrongAnswers;
		this._lastAccessDate = _lastAccessDate;
		this._timeLeft = _timeLeft;
		this._sessionDetails = _sessionDetails;
	}

	public Long get_sessionId() {
		return _sessionId;
	}

	public void set_sessionId(Long _sessionId) {
		this._sessionId = _sessionId;
	}

	public Long get_userId() {
		return _userId;
	}

	public void set_userId(Long _userId) {
		this._userId = _userId;
	}

	public Long get_questionSetId() {
		return _questionSetId;
	}

	public void set_questionSetId(Long _questionSetId) {
		this._questionSetId = _questionSetId;
	}

	public int get_correctAnswers() {
		return _correctAnswers;
	}

	public void set_correctAnswers(int _correctAnswers) {
		this._correctAnswers = _correctAnswers;
	}

	public int get_wrongAnswers() {
		return _wrongAnswers;
	}

	public void set_wrongAnswers(int _wrongAnswers) {
		this._wrongAnswers = _wrongAnswers;
	}

	public Date get_lastAccessDate() {
		return _lastAccessDate;
	}

	public void set_lastAccessDate(Date _lastAccessDate) {
		this._lastAccessDate = _lastAccessDate;
	}

	public long get_timeLeft() {
		return _timeLeft;
	}

	public void set_timeLeft(long _timeLeft) {
		this._timeLeft = _timeLeft;
	}

	public JsonSessionDetails[] get_sessionDetails() {
		return _sessionDetails;
	}

	public void set_sessionDetails(JsonSessionDetails[] _sessionDetails) {
		this._sessionDetails = _sessionDetails;
	}

	@Override
	public String toString() {
		return "JsonSession [_sessionId=" + _sessionId + ", _userId=" + _userId + ", _questionSetId=" + _questionSetId
				+ ", _correctAnswers=" + _correctAnswers + ", _wrongAnswers=" + _wrongAnswers + ", _lastAccessDate="
				+ _lastAccessDate + ", _timeLeft=" + _timeLeft + ", _sessionDetails=" + Arrays.toString(_sessionDetails)
				+ "]";
	}

}
