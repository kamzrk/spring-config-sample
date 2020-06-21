Make alias:

```shell script
> alias vault='docker exec -it vault-dev vault "$@"'
```

Add key:

```shell script
> vault kv put secret/bootiful server.vault.greeting="Greeting from Vault Server"
```