import json
import os

def parse_sarif(file_path):
    """
    Parse un fichier SARIF (rapport SAST CodeQL) et retourne un JSON lisible pour LLM.
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        sarif_data = json.load(f)
    
    # Récupérer toutes les règles des extensions pour les référencer par ruleId
    rule_dict = {}
    for run in sarif_data.get('runs', []):
        extensions = run.get('tool', {}).get('extensions', [])
        for ext in extensions:
            for rule in ext.get('rules', []):
                rule_id = rule.get('id')
                rule_dict[rule_id] = {
                    'name': rule.get('name', 'N/A'),
                    'description': rule.get('fullDescription', {}).get('text', 'N/A'),
                    'severity': rule.get('properties', {}).get('problem.severity', 'N/A'),
                    'security_score': rule.get('properties', {}).get('security-severity', 'N/A'),
                    'tags': rule.get('properties', {}).get('tags', [])
                }

    results_json = []

    # Parcourir les résultats pour chaque vulnérabilité détectée
    for run in sarif_data.get('runs', []):
        for result in run.get('results', []):
            rule_id = result.get('ruleId')
            rule_info = rule_dict.get(rule_id, {})

            # Récupérer fichier et ligne
            locations = result.get('locations', [])
            if locations:
                physical = locations[0].get('physicalLocation', {})
                artifact = physical.get('artifactLocation', {})
                file_path = artifact.get('uri', 'N/A')
                region = physical.get('region', {})
                line = region.get('startLine', 'N/A')
            else:
                file_path = 'N/A'
                line = 'N/A'

            message = result.get('message', {}).get('text', 'N/A')

            # Ajouter à la liste JSON
            results_json.append({
                "rule_id": rule_id,
                "name": rule_info.get('name', 'N/A'),
                "description": rule_info.get('description', 'N/A'),
                "message": message,
                "file": file_path,
                "line": line,
                "severity": rule_info.get('severity', 'N/A'),
                "security_score": rule_info.get('security_score', 'N/A'),
                "tags": rule_info.get('tags', [])
            })

    # Générer le fichier JSON dans le dossier courant
    output_file = os.path.join(os.getcwd(), "report_summary.json")
    with open(output_file, "w", encoding="utf-8") as f:
        json.dump(results_json, f, indent=2, ensure_ascii=False)
    
    print(f"Fichier JSON généré : {output_file}")
    return results_json


# Exemple d'utilisation
if __name__ == "__main__":
    sarif_file = "java.sarif"
    parse_sarif(sarif_file)
