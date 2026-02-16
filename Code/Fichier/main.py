from Code.Fichier.analyse import Analyse
import os


def main():

    # Chemin du fichier CSV situé dans le même dossier que ce script
    base_dir = os.path.dirname(__file__)
    csv_path = os.path.join(base_dir, 'fichier3.csv')

    a = Analyse()
    a.lecture(chemin=csv_path, delim=';')


    print("Données brutes :", a.get_resultat())
    medianes = a.mediane_par_processus()
    
    print("Médianes par processus :")
    for p, stats in medianes.items():
        print(f"  Processus {p} : TPS Médian = {stats['mediane_tps']}, Erreur Médiane = {stats['mediane_erreur']}")

    sp = a.calculer_sp(medianes)
    print("\nCalcul de Sp (selon formule demandée) :")
    for p, valeur in sp.items():
        if p == 1:
            print(f"  Sp{p} (T1/T1) = {valeur}")
        else:
            print(f"  Sp{p} (T1/T{p}) = {valeur}")

    a.affichage_forte(sp)


    '''
    # Chemin du fichier CSV situé dans le même dossier que ce script
    base_dir = os.path.dirname(__file__)
    csv_path = os.path.join(base_dir, 'fichier.csv')

    a = Analyse()
    # Exemple d'appel pour plusieurs fichiers
    fichiers = [csv_path,
                os.path.join(base_dir, 'fichier2.csv'), 
                os.path.join(base_dir, 'fichier3.csv')]
    
    # Vérifier si les fichiers existent avant de les passer
    fichiers_existants = [f for f in fichiers if os.path.exists(f)]
    
    if len(fichiers_existants) > 1:
        print("\nAffichage comparatif (Multi-Faible) :")
        a.affichage_multi_forte(fichiers_existants)
    '''

if __name__ == '__main__':
    main()
