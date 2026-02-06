import matplotlib.pyplot as plt

class Analyse:
    def __init__(self):
        self.donnees = [tuple]
        self.calcul = [tuple]

    def lecture(self, chemin='fichier.csv', delim=';'):
        import csv
        self.donnees = []
        try:
            with open(chemin, newline='', encoding='utf-8') as f:
                reader = csv.reader(f, delimiter=delim)
                for row in reader:
                    self.donnees.append(tuple(row))
        except FileNotFoundError:
            self.donnees = []
        except Exception:
            raise

    def get_resultat(self):
        return self.donnees

    def mediane_par_processus(self):
        import statistics
        # Dictionnaire pour stocker les temps et erreurs par nombre de processus
        stats_par_p = {}
        
        # On suppose que la première ligne est l'en-tête si elle contient du texte non numérique
        # Ou on ignore simplement les lignes qui ne peuvent pas être converties
        for row in self.donnees:
            try:
                p = int(row[2]) # Colonne 'P'
                tps = float(row[3]) # Colonne 'TPS'
                erreur = float(row[4]) # Colonne 'Erreur'
                
                if p not in stats_par_p:
                    stats_par_p[p] = {'tps': [], 'erreur': []}
                stats_par_p[p]['tps'].append(tps)
                stats_par_p[p]['erreur'].append(erreur)
            except (ValueError, IndexError):
                # Ignore l'en-tête ou les lignes mal formées
                continue
        
        # Calcul de la médiane pour chaque P
        medianes = {
            p: {
                'mediane_tps': statistics.median(stats['tps']),
                'mediane_erreur': statistics.median(stats['erreur'])
            }
            for p, stats in stats_par_p.items()
        }
        return medianes

    def calculer_sp(self, medianes):
        # Sp = T1 / Tp (généralement)
        # Mais l'utilisateur demande : p1 -> T1/1, p2 -> T2/T1
        # Je vais suivre strictement l'exemple de l'utilisateur
        
        resultats_sp = {}
        if 1 not in medianes:
            return resultats_sp
            
        t1 = medianes[1]['mediane_tps']
        
        for p, stats in medianes.items():
            tp = stats['mediane_tps']
            if p == 1:
                resultats_sp[p] = tp / tp
            else:
                # Suivant l'exemple : p2 c'est T1/T2
                resultats_sp[p] = t1/tp
                
        return resultats_sp

    def affichage(self, resultats_sp):

        if not resultats_sp:
            print("Aucune donnée Sp à afficher.")
            return

        # Trier par nombre de processus
        processus = sorted(resultats_sp.keys())
        valeurs_sp = [resultats_sp[p] for p in processus]

        plt.figure(figsize=(10, 6))
        plt.plot(processus, valeurs_sp, marker='o', linestyle='-', color='b')

        plt.xlabel('Nombre de processus (P)')
        plt.ylabel('Sp (T1/TP)')
        plt.title('Performance Sp en fonction du nombre de processus')
        plt.grid(True)

        ax = plt.gca()
        ax.set_aspect('equal', adjustable='box')
        plt.xticks(processus)
        
        print("Affichage du graphique...")
        plt.show()




