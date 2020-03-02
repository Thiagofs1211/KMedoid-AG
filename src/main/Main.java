package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	//Number of Clusters. This metric should be related to the number of points
    private int NUM_CLUSTERS = 7;
    
    private List<Point> points;
    private List<Cluster> clusters;
    private double[][] distancias;
    
    private int numeroGeracoes = 5000;
    private int tamanhoPopulacao = 100;
    private int numeroFilhos = 60;
    private int taxaMutacao = 1;
    private AG ag;
    
    public Main() {
    	this.points = new ArrayList<Point>();
    	this.clusters = new ArrayList<Cluster>();    	
    }

	public static void main(String[] args) {
		Main kmedoid = new Main();
    	kmedoid.init();
    	kmedoid.initAG();
    	
    	kmedoid.criaClusters();
    	
    	kmedoid.IdentificaClusters();
    	
    	try {
    		List<Point> dataTeste = Main.carregaAarquivoTeste();
			//List<Point> dataTeste = Main.carregaAarquivoTesteDiabetes();
			kmedoid.testa_base(dataTeste);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void init() {
    	//Create Points
    	try {
			points = carregaAarquivoTreinamento();
    		//points = carregaAarquivoTreinamentoDiabetes();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	int classe0 = 0;
    	int classe1 = 0;
    	
    	for(Point p : points) {
    		if(p.getPoint()[57] == 0) {
    			classe0++;
    		}
    		else {
    			classe1++;
    		}
    	}
    	
    	System.out.println("Número de pontos da classe 0: "+classe0);
    	System.out.println("Número de pontos da classe 1: "+classe1);
    	
    	//Inicializa e calcula matriz de distancia dos pontos
    	distancias = new double[points.size()][points.size()];
    	calculaMatrizDistancia(distancias);
	}
	
	public void initAG() {
    	ag = new AG(NUM_CLUSTERS, points.size(), tamanhoPopulacao);
    	
    	//Calcula Aptidao de cada Individuo da população inicial
    	for(int i = 0; i < tamanhoPopulacao; i++) {
    		ag.calculaAptidao(ag.getPopulacao().get(i), distancias, NUM_CLUSTERS, points.size());
    	}
	}
	
	public void calculaMatrizDistancia(double[][] matriz) {
		for(int i = 0; i < points.size(); i++) {
			for(int j = 0; j <= i; j++) {
				if(i==j) {
					matriz[i][j] = 0;
				} else {
					matriz[i][j] = Point.distance(points.get(i), points.get(j));
					matriz[j][i] = matriz[i][j];
				}
			}
		}
	}
	
	public void mostraMatrizDistancia(double[][] matriz) {
		for(int i = 0; i < points.size(); i++) {
			System.out.println();
			for(int j = 0; j < points.size(); j++) {
				System.out.print(matriz[i][j] + " ");
			}
		}
	}
	
	static List<Point> carregaAarquivoTreinamento()	throws FileNotFoundException, IOException {
		BufferedReader br2 = new BufferedReader(new FileReader("train-data.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("Spam-data.txt"));
		String[] base = new String[4601];
		for(int i = 0;i < 4601; i++) {
			base[i] = br3.readLine();
		}
		br3.close();
		List<Point> point = new ArrayList<Point>();
		String linha[];
		for(int i = 0;i < 3451; i++) {
			Point aux = new Point();
			double[] valor = new double[58];
			linha = base[(int)Double.parseDouble(br2.readLine())].split(",");
			for(int j = 0; j < 58; j++) {
				valor[j] = Double.parseDouble(linha[j]);
			}
			aux.setPoint(valor);
			point.add(aux);
		}
		br2.close();
		return point;
		
	}
	
	static List<Point> carregaAarquivoTeste()	throws FileNotFoundException, IOException {
		BufferedReader br2 = new BufferedReader(new FileReader("test-data.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("Spam-data.txt"));
		String[] base = new String[4601];
		for(int i = 0;i < 4601; i++) {
			base[i] = br3.readLine();
		}
		br3.close();
		List<Point> point = new ArrayList<Point>();
		String linha[];
		for(int i = 0;i < 1150; i++) {
			Point aux = new Point();
			double[] valor = new double[58];
			linha = base[(int)Double.parseDouble(br2.readLine())].split(",");
			for(int j = 0; j < 58; j++) {
				valor[j] = Double.parseDouble(linha[j]);
			}
			aux.setPoint(valor);
			point.add(aux);
		}
		br2.close();
		return point;
	}
	
	static List<Point> carregaAarquivoTreinamentoDiabetes()	throws FileNotFoundException, IOException {
		BufferedReader br2 = new BufferedReader(new FileReader("train-data-diabetes.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("Diabetes-data.txt"));
		String[] base = new String[768];
		for(int i = 0;i < 768; i++) {
			base[i] = br3.readLine();
		}
		br3.close();
		List<Point> point = new ArrayList<Point>();
		String linha[];
		for(int i = 0;i < 576; i++) {
			Point aux = new Point();
			double[] valor = new double[9];
			linha = base[(int)Double.parseDouble(br2.readLine())].split(",");
			for(int j = 0; j < 9; j++) {
				valor[j] = Double.parseDouble(linha[j]);
			}
			aux.setPoint(valor);
			point.add(aux);
		}
		br2.close();
		return point;
		
	}
	
	static List<Point> carregaAarquivoTesteDiabetes()	throws FileNotFoundException, IOException {
		BufferedReader br2 = new BufferedReader(new FileReader("test-data-diabetes.txt"));
		BufferedReader br3 = new BufferedReader(new FileReader("Diabetes-data.txt"));
		String[] base = new String[768];
		for(int i = 0;i < 768; i++) {
			base[i] = br3.readLine();
		}
		br3.close();
		List<Point> point = new ArrayList<Point>();
		String linha[];
		for(int i = 0;i < 192; i++) {
			Point aux = new Point();
			double[] valor = new double[9];
			linha = base[(int)Double.parseDouble(br2.readLine())].split(",");
			for(int j = 0; j < 9; j++) {
				valor[j] = Double.parseDouble(linha[j]);
			}
			aux.setPoint(valor);
			point.add(aux);
		}
		br2.close();
		return point;
	}
	
	public void criaClusters() {
		ag.algoritmoGenetico(numeroGeracoes, points.size(), tamanhoPopulacao, numeroFilhos, taxaMutacao, NUM_CLUSTERS, distancias);
		
		//Consideramos o individuo com melhor aptidão como nossa solucção de agrupamento
		Individuo melhorAgrupamento = ag.getPopulacao().get(0);
		//Achar o medoid de cada cluster desse individuo
		for(int j = 0; j < NUM_CLUSTERS; j++) {
			int menor = -1;
			double distanciaMenor = 0;
			Cluster c = new Cluster(j);
			List<Point> listAux = new ArrayList<Point>();
			for(int k = 0; k < points.size(); k++) {
				if(melhorAgrupamento.getInd()[k] == j) {
					Point aux = new Point();
					aux = points.get(k);
					listAux.add(aux);
					if(menor == -1) {
						menor = k;
						distanciaMenor = ag.calculaDistanciaPontoCluster(j, k, melhorAgrupamento, points.size(), distancias);
					} else {
						double distanciaK = ag.calculaDistanciaPontoCluster(j, k, melhorAgrupamento, points.size(), distancias);
						if(distanciaK < distanciaMenor) {
							menor = k;
							distanciaMenor = distanciaK;
						}
					}
				}
			}
			c.setMedoid(points.get(menor));
			c.setPoints(listAux);
			clusters.add(c);
		}
	}
	
	public void IdentificaClusters() {
		for(Cluster c : clusters) {
			
			int classe0 = 0;
			int classe1 = 0;
			
			for(Point p : c.getPoints()) {
				if(p.getPoint()[57] == 0) {
					classe0++;
				} else {
					classe1++;
				}
			}
			
			double percentual = (double)classe1 / ((double)classe0 + (double)classe1);
			
			if(percentual > 0.39) {
				c.setClasse(1);
			} else {
				c.setClasse(0);
			}
			
			System.out.println("Cluster: "+c.getId());
			System.out.println("Número da classe 0: "+classe0);
			System.out.println("Número da classe 1: "+classe1);
			System.out.println("Classe: "+c.getClasse()+"\n");

		}
	}
	
	private void testa_base(List<Point> dataTest){
        double max = Double.MAX_VALUE;
        double min = max;
        double distance = 0.0;
        int cluster = 0;
        int acerto = 0;
        int erro = 0;
    	for(Point p : dataTest) {
        	min = max;
    		for(Cluster c : clusters) {               
                distance = Point.distance(p, c.getMedoid());
                if(distance < min){
                	min = distance;
                    cluster = c.getId();
    	        }
    	    }
    		p.setCluster(cluster);
    	}
    	for(int i = 0; i < dataTest.size(); i++) {
    		if(clusters.get(dataTest.get(i).getCluster()).getClasse() == dataTest.get(i).getPoint()[8]) {
    			acerto++;
    		} else {
    			erro++;
    		}
    	}
    	/*for(int j = 0; j < dataTest.size(); j++) {
    		for(int i = 0; i < 58; i++) {
    			System.out.print(dataTest.get(j).getPoint()[i]+",");
    		}
    		System.out.println("Classe: "+clusters.get(dataTest.get(j).getCluster()).getClasse());
    	}*/
    	
    	System.out.println("Número de acertos: " + acerto + " - Porcentagem de acerto: " + ((double)acerto/((double)acerto+(double)erro)));
    	System.out.println("Número de erros: "+erro + " - Porcentagem de erro: " + ((double)erro/((double)acerto+(double)erro)));
    }

}
