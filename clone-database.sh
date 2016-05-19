echo "Clone $1 database to $2"
echo "MySQL client parameters: $3"
mysql $3 -e "show databases; create database if not exists $2; show databases;"
rm -rf ~/tmp_db.sql
mysqldump -u admin -pW3lcom3123 -h com-cmg-accenteasy-pronunciation.cnn97htfydqy.ap-southeast-1.rds.amazonaws.com $1 > ~/tmp_db.sql
mysql $3 $2 -e "source ~/tmp_db.sql;"
rm -rf ~/tmp_db.sql
echo "Done"