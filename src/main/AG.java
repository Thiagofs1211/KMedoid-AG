package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AG {
	private List<Individuo> populacao = new ArrayList<Individuo>();
	
	public AG(int numeroCluster, int tamanhoPontos, int tamanhoPopulacao) {
		criarPopulacao(numeroCluster, tamanhoPontos, tamanhoPopulacao);
	}
	
	public void criarPopulacao(int numeroCluster, int numeroPontos, int tamanhoPopulacao) {
		Random r = new Random();
		for(int i = 0; i < tamanhoPopulacao; i++) {
			Individuo ind = new Individuo(numeroPontos);
			for(int j = 0; j < numeroPontos; j++) {
				ind.getInd()[j] = r.nextInt(numeroCluster);
			}
			populacao.add(ind);
		}
	}
	
	public void calculaAptidao(Individuo ind, double[][] matrizDistancia, int numeroCluster, int quantidadePontos) {
		//Achar o medoid de cada cluster para cada individuo
		for(int j = 0; j < numeroCluster; j++) {
			int menor = -1;
			double distanciaMenor = 0;
			for(int k = 0; k < quantidadePontos; k++) {
				if(ind.getInd()[k] == j) {
					if(menor == -1) {
						menor = k;
						distanciaMenor = calculaDistanciaPontoCluster(j, k, ind, quantidadePontos, matrizDistancia);
					} else {
						double distanciaK = calculaDistanciaPontoCluster(j, k, ind, quantidadePontos, matrizDistancia);
						if(distanciaK < distanciaMenor) {
							menor = k;
							distanciaMenor = distanciaK;
						}
					}
				}
			}
			//Aptidao de cada individuo é a samatoria da distancia do medoid para todos os outros pontos do mesmo cluster
			ind.setAptidao(ind.getAptidao() + distanciaMenor);
		}
	}
	
	public double calculaDistanciaPontoCluster(int cluster, int ponto, Individuo ind, int quantidadePontos, double[][] distancias) {
		double soma = 0;
		for(int i = 0; i < quantidadePontos; i++) {
			if(i != ponto && ind.getInd()[i] == cluster) {
				soma = soma + distancias[ponto][i];
			}
		}
		return soma;
	}
	
	public void algoritmoGenetico(int geracoes, int numeroPontos, int tamanhoPopulacao, int numeroFilhos, int taxaMutacao, int numeroClusters, double[][] matrizDistancia) {
		for(int i = 0; i < geracoes; i++) {
			for(int j = 0; j < numeroFilhos/2; j++) {
				//Seleção
				int pai1 = torneio3(tamanhoPopulacao);
				int pai2 = torneio3(tamanhoPopulacao);
				while(pai1 == pai2) {
					pai2 = torneio3(tamanhoPopulacao);
				}
				
				//Crossover
				crossoverDoisPontos(pai1, pai2, numeroPontos, tamanhoPopulacao);
			}
			//Mutação
			mutacaoGene(taxaMutacao, tamanhoPopulacao, numeroFilhos, numeroPontos, numeroClusters);
			
			//Calculo das Aptidões dos filhos
			for(int j = tamanhoPopulacao; j < tamanhoPopulacao + numeroFilhos; j++) {
				calculaAptidao(populacao.get(j), matrizDistancia, numeroClusters, numeroPontos);
			}
			
			//Reinserção da população
			reinsercao(tamanhoPopulacao, numeroFilhos);
		}
	}
	
	public int torneio3(int tamanhoPopulacao) {
		Random r = new Random();
		int sorteio1 = r.nextInt(tamanhoPopulacao);
		int sorteio2 = r.nextInt(tamanhoPopulacao);
		int sorteio3 = r.nextInt(tamanhoPopulacao);
		
		if(populacao.get(sorteio1).getAptidao() < populacao.get(sorteio2).getAptidao() && populacao.get(sorteio1).getAptidao() < populacao.get(sorteio3).getAptidao()) {
			return sorteio1;
		}
		
		if(populacao.get(sorteio2).getAptidao() < populacao.get(sorteio1).getAptidao() && populacao.get(sorteio2).getAptidao() < populacao.get(sorteio3).getAptidao()) {
			return sorteio2;
		}
		
		return sorteio3;
	}
	
	public void crossoverDoisPontos(int pai1, int pai2, int numeroPontos, int tamanhoPopulacao) {
		Random r = new Random();
		int ponto1 = 1 + r.nextInt(tamanhoPopulacao - 1);
		int ponto2 = 1 + r.nextInt(tamanhoPopulacao - 1);
		
		while(ponto1 == ponto2) {
			ponto2 = 1 + r.nextInt(tamanhoPopulacao - 1);
		}
		
		if(ponto1 > ponto2) {
			int aux = ponto1;
			ponto1 = ponto2;
			ponto2 = aux;
		}
		
		Individuo filho1 = new Individuo(numeroPontos);
		Individuo filho2 = new Individuo(numeroPontos);
		
		for(int i = 0; i < numeroPontos; i++) {
			if(i < ponto1 || i > ponto2) {
				filho1.getInd()[i] = populacao.get(pai1).getInd()[i];
				filho2.getInd()[i] = populacao.get(pai2).getInd()[i];
			} else {
				filho1.getInd()[i] = populacao.get(pai2).getInd()[i];
				filho2.getInd()[i] = populacao.get(pai1).getInd()[i];
			}
		}
		
		populacao.add(filho1);
		populacao.add(filho2);
	}
	
	public void mutacaoGene(int taxaMutacao, int tamanhoPopulacao, int numeroFilhos, int numeroPontos, int numeroClusters) {
		Random r = new Random();
		for(int i = tamanhoPopulacao; i < tamanhoPopulacao + numeroFilhos; i++) {
			for(int j = 0; j < numeroPontos; j++) {
				int taxa = r.nextInt(100);
				if(taxa < taxaMutacao) {
					int antigoCluster = populacao.get(i).getInd()[j];
					int novoCluster = r.nextInt(numeroClusters);
					while(antigoCluster == novoCluster) {
						novoCluster = r.nextInt(numeroClusters);
					}
					populacao.get(i).getInd()[j] = novoCluster;
				}
			}
		}
	}
	
	public void reinsercao(int tamanhoPopulacao, int numeroFilhos) {
		Collections.sort(populacao);
		
		for(int i = tamanhoPopulacao; i < tamanhoPopulacao + numeroFilhos; i++) {
			populacao.remove(tamanhoPopulacao);
		}
	}

	public List<Individuo> getPopulacao() {
		return populacao;
	}

	public void setPopulacao(List<Individuo> populacao) {
		this.populacao = populacao;
	}

}