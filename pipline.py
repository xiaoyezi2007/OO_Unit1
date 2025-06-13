import sympy
from xeger import Xeger
import random
import subprocess
from subprocess import STDOUT, PIPE
from gendata import genData


def execute_java(stdin, i):
    cmd = ['java', '-jar', 'hw' + str(i) + '.jar']  # 更改为自己的.jar包名
    proc = subprocess.Popen(cmd, stdin=PIPE, stdout=PIPE, stderr=STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    return stdout.decode().strip()


x = sympy.Symbol('x')
X = Xeger(limit=10)
cnt = 1
flag = 1
while cnt ==1 :
    cnt = cnt + 1
    poly, ans = genData()
    # print("ploy:"+poly)
    # print("ans:"+ans)
    f = sympy.parse_expr(poly)
    poly = poly.replace("**", "^")
    ans = ans.replace("**", "^")
    print(poly + " " + ans)
    for i in [1]:
        strr = execute_java(poly, i)
        strr = strr.replace("^", "**")
        g = sympy.parse_expr(strr)
        strr = strr.replace("**", "^")
        if sympy.simplify(f).equals(g):
            print("AC : " + str(cnt) + " " + str(i))
        else:
            print("!!WA!! with " + str(i) + " " + "poly : " + poly + " YOURS: " + strr + " RIGHT: " + ans)
            flag = 0
