package main;

public class Individuo implements Comparable<Individuo> {
	private int[] ind;
	private double aptidao;
	
	public Individuo(int numeroPontos) {
		ind = new int[numeroPontos];
		this.aptidao = 0;
	}
	
	@Override
	public int compareTo(Individuo ind) {
		if(this.aptidao > ind.getAptidao()) {
			return 1;
		} if(this.aptidao < ind.getAptidao()) {
			return -1;
		}
		return 0;
	}

	public int[] getInd() {
		return ind;
	}

	public void setInd(int[] ind) {
		this.ind = ind;
	}

	public double getAptidao() {
		return aptidao;
	}

	public void setAptidao(double aptidao) {
		this.aptidao = aptidao;
	}
}
