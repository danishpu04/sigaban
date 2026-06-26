import os
import re

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"

files = ["dashboard.html", "detail_warga.html", "form_warga.html", "bantuan.html", "posko.html", "ai_insight.html", "laporan.html", "pengaturan.html"]

labels_by_route = {
    "/dashboard": "Dashboard",
    "/data-warga": "Data Warga",
    "/bantuan": "Bantuan",
    "/posko": "Posko",
    "/ai-insight": "AI Insight",
    "/laporan": "Reports",
    "/pengaturan": "Settings"
}

for file in files:
    path = os.path.join(directory, file)
    if not os.path.exists(path): continue
    
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    for route, label in labels_by_route.items():
        # Handle font-body-md variant (already fixed, but just in case)
        pattern_body = re.compile(r'(href="' + route + r'"[\s\S]*?)<span class="font-body-md text-body-md"</span>')
        content = pattern_body.sub(r'\1<span class="font-body-md text-body-md">' + label + r'</span>', content)
        
        # Handle font-label-md variant (broken in dashboard, detail_warga, form_warga, laporan)
        pattern_label = re.compile(r'(href="' + route + r'"[\s\S]*?)<span class="font-label-md text-label-md"</span>')
        content = pattern_label.sub(r'\1<span class="font-label-md text-label-md">' + label + r'</span>', content)
        
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"Fixed {file}")
