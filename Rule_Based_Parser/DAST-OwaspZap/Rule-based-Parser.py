import json

def parse_dast(file_path):
    """
    Parse un rapport JSON ZAP (DAST) et retourne un résumé JSON structuré.
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        dast_data = json.load(f)

    summary = []

    for site in dast_data.get("site", []):
        site_name = site.get("@name", "N/A")
        host = site.get("@host", "N/A")
        port = site.get("@port", "N/A")
        ssl = site.get("@ssl", "N/A")

        for alert in site.get("alerts", []):
            alert_id = alert.get("pluginid", "N/A")
            alert_name = alert.get("alert", alert.get("name", "N/A"))
            risk = alert.get("riskdesc", "N/A")
            description = alert.get("desc", "N/A")
            solution = alert.get("solution", "N/A")
            cwe = alert.get("cweid", "N/A")
            wasc = alert.get("wascid", "N/A")
            references = alert.get("reference", "N/A")

            # Récupérer les instances spécifiques
            instances_list = []
            for instance in alert.get("instances", []):
                instances_list.append({
                    "uri": instance.get("uri", "N/A"),
                    "method": instance.get("method", "N/A"),
                    "param": instance.get("param", ""),
                    "attack": instance.get("attack", ""),
                    "evidence": instance.get("evidence", ""),
                    "otherinfo": instance.get("otherinfo", "")
                })

            summary.append({
                "site": site_name,
                "host": host,
                "port": port,
                "ssl": ssl,
                "alert_id": alert_id,
                "alert_name": alert_name,
                "risk": risk,
                "description": description,
                "solution": solution,
                "cwe": cwe,
                "wasc": wasc,
                "references": references,
                "instances": instances_list
            })

    return summary

# Exemple d'utilisation
if __name__ == "__main__":
    dast_file = "zap-report.json"  # ton fichier JSON ZAP
    report_summary = parse_dast(dast_file)

    # Écrire le résumé JSON dans un fichier
    with open("dast_summary.json", "w", encoding='utf-8') as out_file:
        json.dump(report_summary, out_file, indent=4, ensure_ascii=False)

    print("Résumé DAST généré dans 'dast_summary.json'")
