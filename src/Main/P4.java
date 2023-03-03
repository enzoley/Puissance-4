package Main;
import java.util.*;

/**
 * @author Enzo Leroyer
 * @version 1.0.1
 */

public class P4 {

	public static int[][] grille;

	public static void initialiseGrille() { 				//Fonction qui initialise la grille en placant un zéro "0" dans chaque case.
		grille = new int[6][7];
		for(int i = 0; i<grille.length; i++) {
			for(int j = 0; j<grille[i].length; j++) {		//On parcourt grille en mettant un 0 à chaque case
				grille[i][j] = 0;
			}
		}
	}

	public static boolean estLibre(int colonne) {			//Fonction qui permet de savoir si la colonne est libre
		for(int i = 0; i<6;i++) {
			if(grille[i][colonne] == 0) {
				return true;								//La fonction retourne true si la colonne est libre
			}
		}
		return false;										//La fonction retourne false si la colonne n'est pas libre
	}

	public static int ligneLibre(int colonne) {				//Fonction qui permet de savoir quelle ligne de la colonne est libre			
		for(int i = 0; i<6;i++) {
			if(grille[i][colonne] == 0) {
				return i;
			}
		}
		return -2;											//Ici on renvoie -1 si il n'y a pas de ligne libre
	}

	public static void jouer(int joueur, int colonne) { 						//Fonction qui implémente la fonction jouer, tout en plaçant le pion sur la case la plus basse possible sur la grille
		int ligne =0;
		while(grille[ligne][colonne] == 1 || grille[ligne][colonne] == 2) { 	//On regarde de bas en haut si un pion est déjà posé sur une case "ligne" de la colonne désirée
			ligne++;
		}
		grille[ligne][colonne] = joueur; 										//Pose du pion sur le couple ("ligne","colonne") dans grille
	}


	public static void joueCoupRandom() { 				//Fonction qui implémente une IA qui joue des coups de manière aléatoire
		boolean libre = false;
		int colonne = 0;
		while(libre != true) { 							//Boucle qui vérifie que chaque coup rentré se situe dans une colonne libre
			libre = false;
			Random random = new Random();
			colonne = random.nextInt(7-0); 				//Choix d'une colonne de façon aléatoire
			for(int i = 0; i<6;i++) {
				if(grille[i][colonne] == 0) {
					libre = true;
				}
			}
		}
		jouer(2,colonne);								 //Pose du pion sur la colonne choisie de manière aléatoire 
	}
	
	public static int peutGagner() { //Fonction qui permet de savoir si l'IA peut gagner en un coup
		int resul = -1;
		for(int i = 0; i<7; i++) {   //On parcout chaque colonne de la grille
			int a  = ligneLibre(i);
			if(a!=-2) {
				jouer(2,i);
			}
			if(aGagne(2)) {			 //On regarde si pour un coup joué dans la colonne la victoire est possible pour l'IA
				resul = i;
			}
			if(a!=-2) {				 //On remet pour finir la case à 0.
				grille[a][i] = 0;
			}
		}
		return resul;
	}

	public static void joueCoupRandom2() { //Fonction qui implémente le deuxième niveau de difficulté de l'IA
		int resul = peutGagner();		   //L'IA regarde si elle peut gagner
		if(resul == -1) {				   //Si elle ne peut pas gagner, elle joue un coup aléatoire
			joueCoupRandom();
		}else {							   //Sinon elle joue le coup gagnant
			jouer(2,resul);
		}
	}
	
	public static int peutPerdre() {	   //Fonction qui permet de savoir si l'IA peut perdre en un coup
		int resul2 = -1;
		for(int i = 0; i<7; i++) {		   //L'IA parcourt les colonnes de la grille
			int a  = ligneLibre(i);
			if(a!=-2) {
				jouer(1,i);
			}
			if(aGagne(1)) {				   //Elle regarde si le coup joué dans la colonne permet au joueur de gagner
				resul2 = i;
			}
			if(a!=-2) {
				grille[a][i] = 0;
			}
		}
		return resul2;
	}

	public static void joueCoupRandom3() { //Fonction qui implémente le troisième niveau de difficulté de l'IA
		int resul = peutGagner();		   //L'IA regarde d'abbord si elle peut gagner en un coup
		if(resul != -1) {
			jouer(2,resul);
		}else {
			int resul2 = peutPerdre();	   //Si elle ne peut pas gagner elle regarde si elle peut perdre au tour suivant
			if(resul2 == -1) {			   //Si elle ne peut ni perdre ni gagner en un coup alors elle joue de manière aléatoire
				joueCoupRandom();
			}else {						   //Sinon elle bloque la possibilité de victoire de son adversaire
				jouer(2,resul2);
			}
		}
	}

	public static void joueCoupRandom4() { //Fonction qui implémente le niveau de difficulté le plus élévé
		int resul = peutGagner();		   //L'IA regarde si elle peut gagner en un coup
		if(resul != -1) {
			jouer(2,resul);
		}else {
			int resul2 = peutPerdre();	   //Si elle ne peut pas gagner, elle regarde si elle peut perdre en un coup
			if(resul2 != -1) {
				jouer(2,resul2);
			}else {						   //Si elle peut ni perdre ni gagner alors elle va jouer de manière à ne pas aider son adversaire à gagner
				int[] resul3 = new int[0];
				for(int i = 0; i<7; i++) {  //On parcourt toutes les colonnes de la grille 
					int a  = ligneLibre(i);
					int b = ligneLibre(i)+1;
					if(a!=-2) {
						jouer(2,i);
						b = ligneLibre(i);
						if(b!=-2) {
							jouer(1,i);
						}
					}
					if(aGagne(1)) {			//L'IA regarde si le coup permet de a son adversaire de gagner 
						resul3 = agrandirTab(resul3); //On ajoute ici le numéro de la colonne à un tableau qui contient toutes les colonnes à exclure
						resul3[resul3.length-1] = i;
					}
					if(a!=-2) {    //Les cases testées sont ensuite remise à 0.
						grille[a][i] = 0;
					}
					if(b!=-1 && b!=-2) {
						grille[b][i] = 0;
					}
				}
				boolean estLibre = false;
				int nbTour = 0;						//Cette variable permet de forcer l'IA à jouer si jamais elle est contrainte de perdre
				int colonne = -1;
				while(estLibre == false && nbTour<5000 && resul3.length != 7) { //L'IA vérifie que la colonne jouée n'est pas une des colonnes à ne pas jouée
					boolean libre = false;
					while(libre != true) { 							//Boucle qui vérifie que chaque coup rentré se situe dans une colonne libre
						libre = false;
						Random random = new Random();
						colonne = random.nextInt(7-0); 				//Choix d'une colonne de façon aléatoire
						for(int i = 0; i<6;i++) {
							if(grille[i][colonne] == 0) {
								libre = true;
							}
						}
					}
					if(appartientTab(resul3,colonne) == false) {	//L'IA regarde si la colonne proposée appartient à la liste des colonnes exclues
						estLibre = true;
					}
					nbTour++;
				}
				jouer(2,colonne);
			}
		}
	}
	
	public static int[] agrandirTab(int[] t) { //Cette fonction permet d'agrandir de 1 un tableau
		int[] t2 = new int[t.length+1];
		for(int i = 0; i<t.length; i++) {
			t2[i] = t[i];
		}
		return t2;
	}
	
	public static boolean appartientTab(int[] t, int cherche) { //Cette fonction permet de vérifier si un nombre est contenu dans un tableau
		for(int i = 0; i<t.length; i++) {
			if(t[i] == cherche) {
				return true;
			}
		}
		return false;
	}

	public static void afficheGrille() { 					//Fonction qui permet d'afficher la grille
		for(int i = 5; i>-1; i--) { 						//Boucle qui parcourt le tableau 2D grille
			for(int k = 0; k<grille[i].length; k++) {		//Boucle qui parcourt chaque ligne de grille[][]
				System.out.print("|"); 						//Structure du plateau de puissance 4
				if(grille[i][k] == 0) { 
					System.out.print(" "); 					//Pose d'un espace si la case est vide(contient un 0 dans grille[][])
				}else if(grille[i][k] == 1) { 
					System.out.print("X"); 					// Pose du pion "X" si la case appartient au joueur 1 (contient un 1 dans grille[][])
				}else {
					System.out.print("O"); 					// Pose du pion "O" si la case appartient au joueur 2 (contient un 2 dans grille[][])
				}
			}
			System.out.print("|"); 							//structure du plateau du puissance 4
			System.out.println();
		}
		for(int j = 0; j<7; j++) {
			System.out.print(" "+j); 						// On pose le numéro de la colonne et un espace pour la structure du plateau
		}
		System.out.println();
	}

	public static boolean aGagneHori(int joueur, int y, int x) { //Fonction qui vérifie si un joueur a aligné 4 pions horizontalement
		if(x+3 <7) {
			if(grille[y][x] == joueur && grille[y][x+1] == joueur && grille[y][x+2] == joueur && grille[y][x+3] == joueur) {
				return true;
			}
		}
		return false;
	}

	public static boolean aGagneVer(int joueur, int y, int x) { //Fonction qui vérifie si un joueur a aligné 4 pions verticalement
		if(y+3<6) {
			if(grille[y][x] == joueur && grille[y+1][x] == joueur && grille[y+2][x] == joueur && grille[y+3][x] == joueur) {
				return true;
			}
		}
		return false;
	}

	public static boolean aGagneDiagMont(int joueur, int y, int x) {//Fonction qui vérifie si un joueur a aligné 4 pions en diagonale montante
		if(y+3<6 && x+3<7) {
			if(grille[y][x] == joueur && grille[y+1][x+1] == joueur && grille[y+2][x+2] == joueur && grille[y+3][x+3] == joueur) {
				return true;
			}
		}
		return false;
	}

	public static boolean aGagneDiagDesc(int joueur, int y, int x) { //Fonction qui vérifie si un joueur aligné 4 pions en diagonale descendante 
		if(y-3>-1 && x+3<7) {
			if(grille[y][x] == joueur && grille[y-1][x+1] == joueur && grille[y-2][x+2] == joueur && grille[y-3][x+3] == joueur) {
				return true;
			}
		}
		return false;
	}

	public static boolean aGagne(int joueur) {						//Cette fonction a pour but de verifier si le joueur "joueur" a gagné
		for(int i =0; i<grille.length; i++) {
			for(int k = 0; k<grille[i].length; k++) {				//On parcourt la grille grâce à deux for
				if(aGagneHori(joueur,i,k)==true) {					//On vérifie chaque condition de victoire et on return true si une condition est vraie
					return true;
				}else if(aGagneVer(joueur,i,k)==true) {
					return true;
				}else if(aGagneDiagMont(joueur,i,k) == true) {
					return true;
				}else if(aGagneDiagDesc(joueur,i,k) == true) {
					return true;
				}
			}
		}
		return false;												//Si on arrive ici c'est que le joueur n'a pas encore gagné alors on return false
	}

	public static boolean matchNul() {					//Cette fonction permet de vérifier sur la grille s'il y a match nul
		for(int i = 0; i<grille.length; i++) {
			for(int j = 0; j<grille[i].length; j++) {   //On parcourt la grille grâce à deux for
				if(grille[i][j] == 0) {					//On regarde si la case est vide, si elle l'est on return false car il faut qu'aucune case ne soit libre pour qu'il y ait match nul
					return false;
				}
			}
		}
		return true;									//Si on arrive ici c'est qu'il y a match nul alors on return true
	}

	public static boolean estUnEntier(String s) {    // Cette fonction a pour objectif de verifier que ce qu'a entré l'utilisateur peut être un Int.
		try {
			Integer.parseInt(s); 				    //On regarde si le string s peut être converti en Int
			return true; 						    // Si la conversion est possible on renvoit "true"
		}catch(NumberFormatException exception){    //Ici le catch permet de ne pas faire planter le programme si le nombre rentré n'est pas un int(erreur du type NumberFormatException)
			return false;                           // On retourne false afin de pouvoir demander de retaper l'entier dans la fonction jeu, ainsi l'erreur n'apparait pas et le programme ne plantera pas
		}
	}

	public static void wait(int time) {				//Cette fonction a pour but de mettre le programme en pause pendant le temps "time"
		try {
			Thread.sleep(time);						//Cette commande permet de mettre en pause le programme pendant un temps "time"
		}catch(InterruptedException excep) {		//Ici on catch l'erreur InterruptedException afin que le programme ne plante pas.
		}
	}

	public static int tirageAuSort() {				//Fonction qui réalise un tirage au sort
		Random random = new Random();
		int toss = 1+random.nextInt(3-1);			//Ici le nombre toss pourra être 1 ou 2
		return toss;								//On retourne ce nombre
	}

	public static void jeu() {                      //Cette fonction contient l'ensemble de la boucle de jeu
		boolean rejouer = true;
		String test ="";
		Scanner sc = new Scanner(System.in);
		System.out.println("Bienvenue au puissance 4 !");                             //Les 4 prochaines lignes affichent des messages de bienvenue quand l'utilisateur lance le jeu
		wait(1000);																	  //On marque des temps de pause pour rendre l'affichage plus agréable
		System.out.println("Le but du jeu est d'aligner 4 pions du même joueur !");
		wait(1000);
		while(rejouer == true) {          //Cette boucle while englobe tout le reste de la fonction, elle permet a la fin de la partie de donner à l'utilisateur le choix de rejouer ou non
			int choix = 0;
			while(choix<1 || choix>2) {   			//Ici cette boucle permet à l'utilisateur de rentrer un nombre (soit 1, soit 2)
				System.out.println("Tapez 1 pour jouer à deux joueurs ou tapez 2 pour jouer contre l'ordinateur : ");
				test = sc.next();
				if(estUnEntier(test) == true) {		//On vérifie que le nombre rentré est bien un entier
					choix = Integer.parseInt(test);
				}
				if(choix<1 || choix>2) {			//Si le nombre rentré est incorrect on renvoie un message d'erreur
					System.out.println("Le nombre entré est incorrect");
				}
			}
			initialiseGrille();
			int tour = 0;
			int joueur;
			int colonne = 7;
			boolean libre = false;
			if(choix == 1) {											//On regarde ici le choix du mode de jeu fait par l'utilisateur
				wait(800);												//Ici, et sur les 5 on réalise un affichage qui introduit le début de la partie
				System.out.println("Le joueur 1 aura les pions X !");
				wait(1000);
				System.out.println("Le joueur 2 aura les pions O !");
				wait(1000);
				System.out.println();
				while(aGagne(1) != true && aGagne(2)!=true && matchNul() != true ) {  //Cette boucle while permet de faire continuer la partie tant qu'un joueur  n'a pas gagné ou si il n'y a pas match nul
					afficheGrille(); //On affiche la grille a chaque tour
					if(tour%2 == 0) {//Ici on regarde quel joueur doit jouer
						joueur = 1;
					}else {
						joueur = 2;
					}
					while(libre != true) { //Boucle qui vérifie que chaque coup rentré se situe dans une colonne libre
						libre = false;
						System.out.println("Quel coup pour le joueur "+joueur+" ?");
						test = sc.next();
						if(estUnEntier(test) == true) {
							colonne = Integer.parseInt(test);
						}
						if(colonne > -1 && colonne <7) { //On vérifie que la colonne existe bien pour ne pas avoir d'erreur du type OutOfBounds qui arretera la partie
							for(int i = 0; i<6;i++) {
								if(grille[i][colonne] == 0) {
									libre = true;
								}
							}
							if(libre == false) { //Message d'erreur en cas de colonne déjà remplie
								System.out.println("La colonne "+ colonne + " est remplie, Veuillez entrer une autre colonne");
							}
						}else if(estUnEntier(test) == false) { //Message d'erreur si le nombre rentré n'est pas un entier
							System.out.println("Veuillez entrer un numéro de colonne correct");
						}else { //Message d'erreur si la colonne n'existe pas
							System.out.println("La colonne "+ colonne + " n'existe pas, Veuillez entrer une autre colonne");
						}
					}
					jouer(joueur,colonne); 								//Le coup du joueur dans la colonne choisie est ajouté à la grille de jeu
					if(aGagne(1) == true) { 							//Ici et sur les prochaines lignes on regarde si un joueur a gagné ou si il y a match nul
						afficheGrille();
						System.out.println("Le joueur 1 a gagné !");
					}else if(aGagne(2) == true) {
						afficheGrille();
						System.out.println("Le joueur 2 a gagné !");
					}else if(matchNul() == true) {
						afficheGrille();
						System.out.println("Match nul !");
					}
					tour++;
					libre = false;
					colonne = 7;
				}
			}else { 				//Si on arrive ici c'est que le joueur a décidé de jouer contre une IA
				wait(300);
				int difficulte = 0;
				while(difficulte<1 || difficulte>4) { //Cette boucle while permet au joueur de choisir son niveau de difficulté
					System.out.println("Veuillez choisir le niveau de difficulté (1-facile, 2-moyen, 3-difficile ou 4-extrême)");
					String testdif = sc.next();
					if(estUnEntier(testdif) == true) {          //On vérifie que le nombre entré est un entier
						difficulte = Integer.parseInt(testdif);
					}
					if(difficulte<1 || difficulte>4) {			//Si le nombre ne correspond pas à un des niveaux de difficulté proposé on renvoie un message d'erreur
						System.out.println("Veuillez entrer un nombre correct");
						wait(200);
					}
				}
				int quiCommence = tirageAuSort(); 						//Ici cet entier prendra la valeur 1 ou 2 ce qui permet de réaliser un tirage au sort de qui commence entre l'IA et le joueur
				wait(800);												//Ici on réalise le même affichage que pour une partie avec deux joueurs
				System.out.println("Le joueur 1 aura les pions X !");
				wait(1000);
				System.out.println("L'ordinateur aura les pions O !");
				wait(1000);
				if(quiCommence == 1) {									//On indique ici à l'utilisateur qui de lui ou de l'IA commence
					System.out.println("Tu commences !");
				}else {
					System.out.println("L'ordinateur commence !");
				}
				wait(800);
				System.out.println();
				while(aGagne(1) != true && aGagne(2)!=true && matchNul() != true ) { //Boucle while qui se poursuit tant que le jeu n'ets pas terminé 
					afficheGrille();
					if(quiCommence == 1) {		//Ici on attribue a qui c'est de jouer en fonction du tirage au sort qui a été fait au début de la partie
						if(tour%2 == 0) {
							joueur = 1;
						}else {
							joueur = 2;
						}
					}else {
						if(tour%2 == 0) {
							joueur = 2;
						}else {
							joueur = 1;
						}
					}
					if(joueur == 1) { //On regarde si c'est au tour du joueur 
						while(libre != true) { //Boucle qui vérifie que chaque coup rentré se situe dans une colonne libre
							libre = false;
							System.out.println("Quel coup pour le joueur "+joueur+" ?");
							test = sc.next();
							if(estUnEntier(test) == true) {
								colonne = Integer.parseInt(test);
							}
							if(colonne > -1 && colonne <7) { //On vérifie que la colonne existe bien pour ne pas avoir d'erreur du type OutOfBounds qui arretera la partie
								for(int i = 0; i<6;i++) {
									if(grille[i][colonne] == 0) {
										libre = true;
									}
								}
								if(libre == false) { //Message d'erreur en cas de colonne déja remplie
									System.out.println("La colonne "+ colonne + " est remplie, Veuillez entrer une autre colonne");
								}
							}else if(estUnEntier(test) == false) {//Message d'erreur si le nombre n'est pas un entier
								System.out.println("Veuillez entrer un numéro de colonne correct");
							}else { //Message d'erreur si la colonne n'existe pas
								System.out.println("La colonne "+ colonne + " n'existe pas, Veuillez entrer une autre colonne");
							}
						}
						jouer(joueur,colonne); //On ajoute le coup à la grille
					}else { //Si on arrive ici c'est que c'est au tour de l'IA
						System.out.println();									//On réalise ici une animation qui montre que la réflexion de l'IA
						System.out.print("L'ordinateur réfléchit à son coup");
						for(int i = 0;i<5;i++) {
							wait(300);
							if(i<4) {
								System.out.print(".");
							}else {
								System.out.println(".");
							}

						}
						System.out.println();
						wait(200);
						if(difficulte == 1) {       //Ici on fait jouer l'IA en fonction du niveau de difficulté choisi par l'utilisateur
							joueCoupRandom();
						}else if(difficulte == 2) {
							joueCoupRandom2();
						}else if(difficulte == 3){
							joueCoupRandom3();
						}else {
							joueCoupRandom4();
						}
					}
					if(aGagne(1) == true) { 							//On vérifie si un joueur a gagné ou si il y a match nul
						afficheGrille();
						System.out.println("Le joueur 1 a gagné !");
					}else if(aGagne(2) == true) {
						afficheGrille();
						System.out.println("L'ordinateur a gagné !");
					}else if(matchNul() == true) {
						afficheGrille();
						System.out.println("Match nul !");
					}
					tour++;
					libre = false;
					colonne = 7;
				}
			}
			int rej = 0;
			wait(1000);
			while(rej<1 || rej>2) { // Cette boucle permet de proposer à l'utilisateur de rejouer
				System.out.println("Tapez 1 pour rejouer ou tapez 2 pour quitter le jeu");
				test = sc.next();
				if(estUnEntier(test) == true) {   //On vérifie que le nombre rentré est un entier
					rej = Integer.parseInt(test);
				}
				if(estUnEntier(test) == false || rej<1 || rej>2) {   		//Message d'erreur si le nombre ne correspond pas 
					System.out.println("Veuillez entrer un nombre correct");
				}
				if(rej == 1) { //Ici si le joueur a choisi de rejouer on laisse rejouer a true
					wait(500);
				}
				if(rej == 2) {//Ici si le joueur a choisi d'arreter de jouer on affiche un message et on met rejouer à false pour que l'utilisateur sorte de la boucle de jeu
					wait(200);
					System.out.println("A bientôt ! ");
					rejouer = false;
				}
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		jeu(); //Ici le main ne contient que la boucle de jeu car celle ci contient tout les élèments du jeu
	}
}