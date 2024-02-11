import os
import time
import platform


try:

    PATH = 'LAB5/'
    JAR_ARG = 'test.csv'


    fl = 0

    while (1):
        os.system('CLS' if platform.system() == 'Windows' else "clear")
        print('==============================')
        print('scan...', end ='', flush=True)
        if not os.path.exists('_dev'):
            os.makedirs('_dev')
        if (os.path.exists('input.txt') == False):
            f = open("input.txt", "w")
            f.close()
        jars = []
        javas = []
        MF = ''
        newt = 0
        for rootdir, dirs, files in os.walk(PATH):
            for file in files:
                dir = rootdir[len(PATH):]
                dirfile = os.path.join(rootdir, file)
                if((file.split('.')[-1])=='jar'):
                    if fl == 0:
                        os.system('(mkdir "'+dir+'") >_dev/copy_output.txt 2>_dev/copy_error.txt')
                        os.system('(copy "'+dirfile+'" "'+dir+'/'+file+'" /Y) >_dev/copy_output.txt 2>_dev/copy_error.txt')
                    jars.append('lib/'+file)
                if((file.split('.')[-1])=='java'):
                    javas.append(dirfile)
                if((file.split('.')[-1])=='mf'):
                    MF = dirfile

        t = newt
        
        jars = ';'.join(jars);
        if jars != '':
            jars = ' -classpath ' + jars+''
           
        javas = ' '.join(javas);
        if javas == '':
            print('NOT FOUND JAVAFILES')
            if (input()=='EXIT'):
                break;
            continue;
        print('OK')



        print('javac...', end ='', flush=True)
        if (os.system('cmd /c "(javac'+jars+' '+javas+' -d classes) >_dev/javac_output.txt 2>_dev/javac_error.txt"' if platform.system() == 'Windows' else 'bash -c "(javac'+jars+' '+javas+' -d classes) >_dev/javac_output.txt 2>_dev/javac_error.txt"')>0):
            print('=========== JAVAC ERROR ===========')
            print("\n".join(open("_dev/javac_error.txt", "r").read().split("\n")[:20]))
            fl = 0
            t = 0
            if (input()=='EXIT'):
                break;
            continue;
        print('OK')



        print('jar...', end ='', flush=True)
        jar_command = ''
        if MF != '':
            jar_command = 'jar -cvfm app_client.jar '+MF+' -C classes .'
        else:
            jar_command = 'jar -cvf app_client.jar -C classes .'

        if (os.system('cmd /c "('+jar_command+') >_dev/jar_output.txt 2>_dev/jar_error.txt"' if platform.system() == 'Windows' else 'bash -c "('+jar_command+') >_dev/jar_output.txt 2>_dev/jar_error.txt"')>0):
            print('=========== JAR ERROR ===========')
            print(open("_dev/jar_error.txt", "r").read())
            fl = 0
            t = 0
            if (input()=='EXIT'):
                break;
            continue;
        print('OK')



        print('java...', end ='', flush=True)
        if (os.system('cmd /c "(java -jar app_client.jar '+JAR_ARG+') < input.txt >_dev/java_output.txt 2>_dev/java_error.txt"'  if platform.system() == 'Windows' else 'bash -c "(java -jar app.jar '+JAR_ARG+') < input.txt >_dev/java_output.txt 2>_dev/java_error.txt"')>0):
            print('=========== JAVA ERROR ===========')
            print('=========== output ===========')
            print(open("_dev/java_output.txt", "r").read())
            print('=========== error ===========')
            print(open("_dev/java_error.txt", "r").read())
            if (input()=='EXIT'):
                break;
            continue;
        print('OK')



        print('=========== output ===========')
        print(open("_dev/java_output.txt", "r").read())
        fl += 1;
        if (input()=='EXIT'):
            break;
except BaseException as e:
    print('FATAL ERROR')
    print(str(e))
    print('close 3')
    time.sleep(1)
    print('close 2')
    time.sleep(1)
    print('close 1')
    time.sleep(1)