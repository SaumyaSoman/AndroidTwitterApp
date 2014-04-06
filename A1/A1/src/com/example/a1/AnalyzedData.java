package com.example.a1;

public class AnalyzedData {
	
	int positive=0;
	int negative=0;
	int mixed=0;
	String name;
	double score;
	public int getPositive() {
		return positive;
	}
	public void setPositive(int positive) {
		this.positive = positive;
	}
	public int getNegative() {
		return negative;
	}
	public void setNegative(int negative) {
		this.negative = negative;
	}
	public int getMixed() {
		return mixed;
	}
	public void setMixed(int mixed) {
		this.mixed = mixed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "AnalyzedData [positive=" + positive + ", negative=" + negative
				+ ", mixed=" + mixed + ", name=" + name + ", score=" + score
				+ "]";
	}
	
	

}
