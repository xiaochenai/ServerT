import os

Round = 0
RoundNumner=5
f = open('Finish.txt')
result=f.readline()
while(Round < RoundNumner):
    if result == 'Unfini':
        os.system('java Sender 127.0.0.1 2000')
        print 'This is Round',Round
        Round = Round + 1
