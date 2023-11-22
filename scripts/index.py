import os


def log(content):
	f = open("test.txt", "a")
	f.write(content+"\n")
	f.close()

log("test")
os.system('say "your program has finished"')
