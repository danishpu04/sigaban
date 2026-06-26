import os
import re

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"

files_to_fix = [
    "dashboard.html",
    "detail_warga.html",
    "form_warga.html",
    "bantuan.html",
    "posko.html",
    "ai_insight.html",
    "laporan.html",
    "pengaturan.html"
]

routes = {
    "Dashboard": "/dashboard",
    "Data Warga": "/data-warga",
    "Bantuan": "/bantuan",
    "Posko": "/posko",
    "AI Insight": "/ai-insight",
    "Reports": "/laporan",
    "Settings": "/pengaturan"
}

for filename in files_to_fix:
    filepath = os.path.join(directory, filename)
    if not os.path.exists(filepath):
        print(f"Skipping {filename} - not found")
        continue
        
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    # We want to replace href="#" or href="/..." in the sidebar.
    # The sidebar links typically look like:
    # <a class="..." href="#">
    #   <span class="material-symbols-outlined">...</span>
    #   <span class="...">Dashboard</span>
    # </a>
    
    # We can search for the text inside the span and replace the closest preceding href.
    # A robust way is to find the <li> block.
    
    def replace_href(match):
        full_match = match.group(0)
        link_text = match.group(3).strip()
        if link_text in routes:
            new_href = routes[link_text]
            # Replace the href inside this block
            # href="..." -> href="new_href"
            replaced = re.sub(r'href="[^"]*"', f'href="{new_href}"', full_match)
            return replaced
        return full_match

    # Regex to capture the <a> tag and the inner text of the span containing the label
    # e.g., <a ... href="..."> ... <span>Dashboard</span> ... </a>
    pattern = re.compile(r'(<a[^>]*href=")([^"]*)("[^>]*>[\s\S]*?<span[^>]*>([^<]+)</span>[\s\S]*?</a>)')
    
    # Actually, the span might have child elements? No, it's <span class="...">Dashboard</span>.
    # Let's refine the regex.
    # We'll just replace based on the known labels.
    for label, route in routes.items():
        # Look for </a> that contains the label
        # This is a bit tricky with regex. Let's do a simple approach.
        # Find index of the label.
        
        parts = content.split(f">{label}<")
        if len(parts) > 1:
            for i in range(len(parts) - 1):
                # Search backwards from the label for the last href="
                part = parts[i]
                last_href_idx = part.rfind('href="')
                if last_href_idx != -1:
                    # Find the end of the href value
                    end_quote_idx = part.find('"', last_href_idx + 6)
                    if end_quote_idx != -1:
                        # Replace the value
                        parts[i] = part[:last_href_idx + 6] + route + part[end_quote_idx:]
            
            content = ">" + label + "<".join(parts)
            # wait, the string joining logic is slightly flawed.
            
    # Better approach: parse and replace using simple regex per label
    new_content = content
    for label, route in routes.items():
        # Match from href=" up to >label<
        # This requires matching href="...", then any chars until >label<
        # We need to make sure we don't match across multiple <a> tags.
        pattern = re.compile(r'href="([^"]*)"([^>]*>[\s\S]{0,100}?>)' + label + r'<')
        new_content = pattern.sub(f'href="{route}"\\g<2>{label}<', new_content)
        
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)
        
    print(f"Updated {filename}")
