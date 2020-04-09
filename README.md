## BUILD

From parent level directory run:

```mvn clean install```

Because services use shared code from the utils module the build has to happen from the parent directory
and not from the individual module directories.

### Configuration

- All microservice URIs are configured via OS environment variables