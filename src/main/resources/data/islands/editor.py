import json

def adjust_base_value(filename : str, multiplier : float):
    
    infile = open(filename)
    lines = infile.read()
    items = json.loads(lines)

    out_items = []

    for item in items:
        item['base_value'] *= multiplier
        out_items.append(item)

    outfile = open(filename, 'w')

    outfile.writelines(json.dumps(out_items))

    infile.close()
    outfile.close()

adjust_base_value('cargo.json', 2)
