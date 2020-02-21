Info:
GitHub link: https://github.com/paavoma/packagedependancy-htmlexplorer

On a Debian and Ubuntu systems, there is a file called /var/lib/dpkg/status that holds information about software packages that the system knows about.
This program, packagedependacy-htmlexplorer exposes following key information about those packages via HTML-interface:
- Package name
- Dependencies (packages that this package requires)
- Reverse dependencies (packages that require this package)
- Description

If the folder is not available, a mock-up data file (status.real) is used to generate the the HTML-pages

Program Javadoc API can be found in https://github.com/paavoma/packagedependancy-htmlexplorer/doc

Installing:
Extract package-explorer.zip to desired folder

How to use:
Double click package-explorer.jar in the install folder. The program generates web pages in the program installing folder. The program should automatically start your
default browser. If the running the program doesn't auto-open in your default browser, go to the generated /html folder in the programs install folder and open index.html 