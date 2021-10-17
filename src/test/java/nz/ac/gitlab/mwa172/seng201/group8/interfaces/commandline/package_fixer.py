import os



def clean_file(filename):
    reader = open(filename, 'r')
    lines = reader.readlines()
    writer = open(filename, 'w')
    print(lines)
    for line in lines:
        if 'main.java.' in line:
            line = line.replace('main.java.', '')
        if 'test.java' in line:
            line = line.replace('test.java.', '')
        writer.write(line)
    writer.close()
    reader.close()

for filename in os.listdir():
    if filename.endswith(".java"):
        clean_file(filename)
