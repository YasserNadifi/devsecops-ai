import json
import re
from html import unescape

def strip_html(text):
    """Nettoie les balises HTML simples pour le rendre lisible."""
    if not text:
        return ""
    text = re.sub(r"<[^>]+>", "", text)
    return unescape(text).strip()

# ---------------- DAST ---------------- #
def format_dast(file_path):
    """Transforme le résumé DAST en texte lisible."""
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            dast_data = json.load(f)
    except Exception as e:
        print(f"[WARN] Impossible de lire {file_path}: {e}")
        return "Aucune donnée DAST trouvée.\n"

    output = ["==== 🔍 DAST Vulnerabilities (ZAP Report) ====\n"]
    for alert in dast_data:
        output.append(f"🌐 Site: {alert.get('site')} (host={alert.get('host')} port={alert.get('port')} ssl={alert.get('ssl')})")
        output.append(f"⚠️  {alert.get('alert_name')} — Risk: {alert.get('risk')}")
        output.append(f"📄 Description: {strip_html(alert.get('description'))}")
        output.append(f"💡 Solution: {strip_html(alert.get('solution'))}")
        output.append(f"📚 CWE: {alert.get('cwe')} | WASC: {alert.get('wasc')}")
        output.append(f"🔗 References: {strip_html(alert.get('references'))}")
        output.append("📍 Instances:")
        for i, inst in enumerate(alert.get("instances", []), start=1):
            output.append(f"   {i}. {inst.get('method')} {inst.get('uri')} | evidence: {inst.get('evidence')}")
        output.append("-" * 60)
    return "\n".join(output)


# ---------------- SAST ---------------- #
def format_sast(file_path):
    """Transforme le résumé SARIF (CodeQL) en texte lisible."""
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            sast_data = json.load(f)
    except Exception as e:
        print(f"[WARN] Impossible de lire {file_path}: {e}")
        return "Aucune donnée SAST trouvée.\n"

    output = ["\n==== 🧩 SAST Vulnerabilities (CodeQL Report) ====\n"]
    for v in sast_data:
        output.append(f"📘 Rule: {v.get('name')} (ID: {v.get('rule_id')})")
        output.append(f"📄 Description: {v.get('description')}")
        output.append(f"💬 Message: {v.get('message')}")
        output.append(f"📍 File: {v.get('file')} (Line: {v.get('line')})")
        output.append(f"⚠️ Severity: {v.get('severity')} | Security Score: {v.get('security_score')}")
        output.append(f"🏷️ Tags: {', '.join(v.get('tags', []))}")
        output.append("-" * 60)
    return "\n".join(output)


# ---------------- SCA ---------------- #
def format_sca(file_path):
    """Transforme le rapport Snyk (SCA) en texte lisible."""
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            sca_data = json.load(f)
    except Exception as e:
        print(f"[WARN] Impossible de lire {file_path}: {e}")
        return "Aucune donnée SCA trouvée.\n"

    output = ["\n==== 🧱 SCA Vulnerabilities (Snyk Report) ====\n"]
    for v in sca_data:
        output.append(f"📦 Package: {v.get('package')}")
        output.append(f"⚠️  {v.get('type')} (Severity: {v.get('severity')})")
        output.append(f"🧾 Description: {v.get('description')}")
        output.append(f"💡 Recommendation: {v.get('recommendation')}")
        output.append(f"🆔 CWE: {', '.join(v.get('CWE', []))} | CVE: {', '.join(v.get('CVE', []))}")
        output.append("-" * 60)
    return "\n".join(output)


# ---------------- MAIN ---------------- #
if __name__ == "__main__":
    dast_prompt = format_dast("dast_summary.json")
    sast_prompt = format_sast("sast_summary.json")
    sca_prompt = format_sca("parsed_snyk_vulnerabilities.json")

    # Fusion finale du prompt
    final_prompt = (
        "=== SECURITY SCAN CONSOLIDATED REPORT ===\n\n"
        + dast_prompt + "\n"
        + sast_prompt + "\n"
        + sca_prompt + "\n"
        + "=== END OF REPORT ==="
    )

    with open("llm_prompt_security.txt", "w", encoding="utf-8") as f:
        f.write(final_prompt)

    print("\n✅ Prompt lisible généré dans 'llm_prompt_security.txt'")
