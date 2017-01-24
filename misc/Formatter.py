##
# @file 	  Formatter.py
# @author     Mikhail Andrenkov
# 
# @brief      Adds appropriate Doxygen headers to the Java source files.
#

import datetime, os, re, sys


''' Regular Expressions '''


## Java file extensions
RE_EXTENSION = re.compile(r"\.java")
## First source code line
RE_FIRST = re.compile(r"^\s*package.*$")
## Empty line
RE_EMPTY = re.compile(r"^\s*$")

## Java class (or interface)
RE_CLASS = re.compile(r"^\s*(public|private|protected)?\s*(abstract)?\s*(class|interface)\s+(?P<Class>[a-zA-Z]+)({|\s+)")

## First commented line
RE_COMMENT_START = re.compile(r"^\s*(//|.*\*/).*$")
## Commented line
RE_COMMENT_MIDEND = re.compile(r"^\s*(//|.*\*/|\*).*$")

## Commented line
RE_DOC_SINCE   = re.compile(r"^\s*\*\s*since$")
RE_DOC_VERSION = re.compile(r"^\s*(//|.*\*/|\*).*$")



''' Constants '''

#Date
DATE = datetime.datetime.today().strftime('%B %d, %Y')
# Version
VERSION = "1.0"



''' Classes '''


class JavaFile:
	def __init__(self, path, content):
		self.path = path
		self.content = content
		
	def addHeader(self):
		#self.__removeClassComment()

		classResult = self.__getClass()
		if classResult:
			classLine, className = classResult

			header = []
			header.append("/**")
			header.append(" * @author Mikhail Andrenkov")
			header.append(" * @since %s" % DATE)
			header.append(" * @version %s" % VERSION)
			header.append(" *")
			header.append(" * <p>Member declarations and definitions for the <b>%s</b> class.</p>" % className)
			header.append(" */ ")

			self.content = self.content[:classLine] + header + self.content[classLine:]

	def getContent(self):
		return "\n".join(self.content)

	def getPath(self):
		return self.path

	def trimVertical(self):
		indexA = 0
		indexB = len(self.content) - 1

		while indexA < len(self.content):
			line = self.content[indexA]
			if RE_FIRST.search(line):
				break
			indexA += 1

		while indexB >= 0:
			line = self.content[indexB]
			if not RE_EMPTY.search(line):
				break
			indexB -= 1

		self.content = self.content[indexA:indexB + 1]

	def updateHeader(self):
		classResult = self.__getClass()
		if classResult:
			classLine, className = classResult

			if not RE_COMMENT_MIDEND.search(self.content[classLine - 1]):
				self.addHeader()

			sinceLine = self.__findDocTag("since")
			versionLine = self.__findDocTag("version")

			self.content[sinceLine]   = " * @since %s" % DATE
			self.content[versionLine] = " * @version %s" % VERSION


	def __findDocTag(self, tag):
		RE_TAG = re.compile(r"^\s*\*\s*@%s.*$" % tag)

		for index in xrange(len(self.content)):
			line = self.content[index]
			if RE_TAG.search(line):
				return index

		return -1

	def __getClass(self):
		for index in xrange(len(self.content)):
			line = self.content[index]
			classResult = RE_CLASS.search(line)
			if classResult:
				className = classResult.group("Class").strip()
				return (index, className)

		return None

	def __removeClassComment(self):
		classResult = self.__getClass()
		if classResult:
			classLine, className = classResult
			classComment = classLine - 1

			while classComment >= 0:
				line = self.content[classComment]

				if RE_COMMENT_START.search(line):
					classComment -= 1
					break
				elif not RE_COMMENT_MIDEND.search(self.content[classComment]):
					break

				classComment -= 1

			self.content = self.content[:classComment + 1] + self.content[classLine:]

	def __str__(self):
		return "%s:\n---\n%s" % (self.path, "\n".join(self.content))


''' Methods '''


##
#  @brief      Formats all of the given Java source files
# 
#  @param      javaFiles  The Java source files
#
def formatJavaFiles(javaFiles):
	for javaFile in javaFiles:
		javaFile.trimVertical()
		javaFile.updateHeader()

		with open(javaFile.getPath(), "w") as fileOut:
			fileOut.write(javaFile.getContent())


##
#  @brief      Recursively finds all Java source files
#
def findJavaFiles():
	javaFiles = []
	exploreDirs = ["../src"]

	while exploreDirs:
		currentDir = exploreDirs.pop()

		allFiles = os.listdir(currentDir)

		for nextDir in filter(lambda name: os.path.isdir(currentDir + "/" + name), allFiles):
			exploreDirs.append(currentDir + "/" + nextDir)

		for nextFile in filter(lambda name: not os.path.isdir(currentDir + "/" + name), allFiles):
			if RE_EXTENSION.search(nextFile):
				javaPath = currentDir + "/" + nextFile

				content = None
				with open(javaPath, "r") as fileIn:
					content = map(lambda s: s.rstrip(), fileIn.readlines())

				javaFiles.append(JavaFile(javaPath, content))

	return javaFiles

##
#  @brief      Execution entry point
#
def main():
	javaFiles = findJavaFiles()
	print "Formatting %d Java files ..." % len(javaFiles),
	formatJavaFiles(javaFiles)
	print "Done."

if __name__ == "__main__":
	main()
