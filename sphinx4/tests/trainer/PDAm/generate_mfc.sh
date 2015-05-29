echo "======================================"
echo "Extract MFC file"

rm -rf **/*.mfc

sphinx_fe -argfile \
	feat.params \
	-samprate 16000 \
	-c PDAm.fileids \
	-di . \
	-do . \
	-ei wav \
	-eo mfc \
	-mswav yes
