import os

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"
files = ["dashboard.html", "detail_warga.html", "form_warga.html", "bantuan.html", "posko.html", "ai_insight.html", "laporan.html", "pengaturan.html"]

junk = ">Settings>Reports>AI Insight>Posko>Bantuan>Data Warga>Dashboard<"

for file in files:
    path = os.path.join(directory, file)
    if not os.path.exists(path): continue
    
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
        
    if content.startswith(junk):
        content = content[len(junk):]
        with open(path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Removed junk from {file}")
    else:
        print(f"No junk found in {file}")
