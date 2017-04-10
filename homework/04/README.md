# MongoDB project

[Install MongoDB on macOS](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/) with Homebrew, and set up:

```shell
> brew install mongodb
> cd <project directory>
> mkdir -p /data/db  # Create MongoDB's data directory
> whoami
tom
> chown tom /data/db  # Set permissions
> mongoimport --db local --collection test --drop --file ./data.json --jsonArray  # Add data to MongoDB
> mongo 127.0.0.1:27017/local project.js  # Run homework script
```
