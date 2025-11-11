import json

def count_vulnerabilities(file_path):
    """
    Compte le nombre de vulnérabilités détectées dans un fichier SARIF.
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        sarif_data = json.load(f)
    
    vuln_count = 0
    for run in sarif_data.get('runs', []):
        vuln_count += len(run.get('results', []))
    
    return vuln_count

# Exemple d'utilisation
if __name__ == "__main__":
    sarif_file = "java.sarif"
    total_vulns = count_vulnerabilities(sarif_file)
    print(f"Nombre total de vulnérabilités détectées : {total_vulns}")