#!/bin/sh

wavfile="models/phrase1.wav";


# mfc feature extraction
mfcfile=`echo ${wavfile} | sed "s:.wav:.mfc:g"`;

scppath=/Volumes/DATA/CMG/git/pronunciation/speecheval/server/datacollection/models

sphinx_fe -i ${wavfile} -o ${mfcfile} -argfile ${scppath}/feat.params -mswav yes

if [ $? -gt 0 ]; then
	echo "MFCC feature extraction error!";
	return 1;
fi

nosuffix=`echo ${wavfile} | sed "s:.wav::g"`;
filename=`basename ${nosuffix}`;

ctlfile=${nosuffix}.ctl
echo ${nosuffix} > ${ctlfile}

mkdir -p ${nosuffix}{.phonelabdir,.phonesegdir,.statesegdir,.aligndir}

sphinx3_align -hmm /Volumes/DATA/Development/Sphinx/wsj-en-us -dict ${scppath}/cmu.dic -fdict ${scppath}/phone.filler -ctl ${ctlfile} -insent ${scppath}/sents/${filename}.insent -outsent ${nosuffix}.outsent -phlabdir ${nosuffix}.phonelabdir -phsegdir ${nosuffix}.phonesegdir -stsegdir ${nosuffix}.statesegdir -wdsegdir ${nosuffix}.aligndir

if [ $? -gt 0 ]; then
	echo "Alignment error!";
	return 1;
fi

if [ ! -e ${nosuffix}.phonesegdir/${filename}.phseg ]; then
	echo "No alignment results could be found!";
	return 1;
fi

# edit-distance decoding

sphinx3_decode -hmm /Volumes/DATA/Development/Sphinx/wsj-en-us -fsg ${scppath}/fsgs/${filename}.fsg -dict ${scppath}/phone.dic -fdict ${scppath}/phone.filler -ctl ${ctlfile} -hyp ${nosuffix}.rec -mode allphone -op_mode 2

if [ $? -gt 0 ]; then
	echo "Decoding error!";
	return 1;
fi

# post-processing to get the accuracy
python ${scppath}/compute_wer.py ${nosuffix}.phonesegdir/${filename}.phseg ${nosuffix}.rec ${nosuffix}.per

if [ $? -gt 0 ]; then
	echo "Computing PER error!";
	return 1;
fi

