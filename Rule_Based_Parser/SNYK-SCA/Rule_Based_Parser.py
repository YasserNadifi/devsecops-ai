import json
import os

def parse_snyk_sca_report(path):
    if not os.path.exists(path):
        print(f"[WARN] Report not found at {path}")
        return []

    with open(path, "r") as f:
        data = json.load(f)

    vulnerabilities = []
    for v in data.get("vulnerabilities", []):
        vulnerabilities.append({
            "tool": "Snyk",
            "vulnerability_id": v.get("id", "N/A"),
            "type": v.get("title", "N/A"),
            "severity": v.get("severity", "Unknown").capitalize(),
            "package": v.get("packageName", v.get("moduleName", "N/A")),
            "description": v.get("description", "N/A").replace("\n", " "),
           "recommendation": ", ".join(str(item) for item in v.get("upgradePath", [])) if v.get("upgradePath") else "Upgrade to secure version",
            "CVE": v.get("identifiers", {}).get("CVE", []),
            "CWE": v.get("identifiers", {}).get("CWE", [])
        })

    return vulnerabilities

# ---------- MAIN ----------
if __name__ == "__main__":
    snyk_report_path = "snyk-backend-result.json"
    parsed_vulns = parse_snyk_sca_report(snyk_report_path)

    # Sauvegarde dans un fichier JSON utilisable par LLM
    output_file = "parsed_snyk_vulnerabilities.json"
    with open(output_file, "w") as f:
        json.dump(parsed_vulns, f, indent=2)

    print(f"[INFO] {len(parsed_vulns)} vulnerabilities parsed from Snyk report")
    print(f"[INFO] Output saved to {output_file}")
