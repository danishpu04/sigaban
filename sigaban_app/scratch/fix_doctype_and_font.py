import os

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"
files = [
    "dashboard.html", "detail_warga.html", "form_warga.html", 
    "bantuan.html", "posko.html", "ai_insight.html", 
    "laporan.html", "pengaturan.html", "landing_page.html"
]

inter_font_link = '<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&amp;display=swap" rel="stylesheet"/>'

for filename in files:
    filepath = os.path.join(directory, filename)
    if not os.path.exists(filepath): continue
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    changed = False
    
    # Fix !DOCTYPE html> to <!DOCTYPE html>
    if content.startswith("!DOCTYPE html>"):
        content = "<" + content
        changed = True
        
    # Ensure Inter font is included in <head>
    if "family=Inter:" not in content:
        # Find </head> or a place to inject it
        head_end_idx = content.find("</head>")
        if head_end_idx != -1:
            content = content[:head_end_idx] + '    ' + inter_font_link + '\n' + content[head_end_idx:]
            changed = True
            
    if changed:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Fixed {filename}")
    else:
        print(f"No changes needed for {filename}")
