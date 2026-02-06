from Code.Fichier.analyse import Analyse
import os


def main():
    # Chemin du fichier CSV situé dans le même dossier que ce script
    base_dir = os.path.dirname(__file__)
    csv_path = os.path.join(base_dir, 'fichier.csv')

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

    a.affichage(sp)







if __name__ == '__main__':
    main()
