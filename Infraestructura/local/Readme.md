chmod +x localstack-init/10-create-secret.sh

docker exec -it msfranchises-localstack awslocal secretsmanager put-secret-value \
 ms-franchises/m>   --secret-id ms-franchises/mysql \
>   --secret-string '{"host":"127.0.0.1","port":3307,"dbname":"franchises","username":"franchises_user","password":"admin"}'
{
    "ARN": "arn:aws:secretsmanager:us-east-1:000000000000:secret:ms-franchises/mysql-bHMbhK",
    "Name": "ms-franchises/mysql",
    "VersionId": "b3185a0c-1cb7-4d64-9c04-e451c4cb5ed5",
    "VersionStages": [
        "AWSCURRENT"
    ]
