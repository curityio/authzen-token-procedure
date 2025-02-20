# AuthZEN Token Procedure Plugin
[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

A custom Token Procedure plugin for the Curity Identity Server.

- If token is allowed to be issued
- If requested scope(s) is/are allowed
- If user is allowed to be issued token for the given client/app

## Building the Plugin

You can build the plugin by issuing the command ``mvn package``. This will produce a JAR file in the ``target`` directory,
which can be installed.

## Installing the Plugin

To install the plugin, copy the compiled JAR (and all of its dependencies) into the :file:`${IDSVR_HOME}/usr/share/plugins/AuthZENTokenProcedure`
on each node, including the admin node. For more information about installing plugins, refer to [curity.io/plugins](https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html#plugin-installation).

## Required Dependencies

For a list of the dependencies and their versions, run ``mvn dependency:list``. Ensure that all of these are installed in
the plugin group; otherwise, they will not be accessible to this plug-in and run-time errors will result.

## Configuration

### Authorization Configuration
| Parameter          | Type    | Description                                                                                                                                                                                                                                             | Default |
|--------------------|---------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| `Authorize Client` | Boolean | This setting will pass the ID of client used for the flow to be passed as a resource attribute to the PDP. It is expected that the PDP will have a policy in place to determine of this client is allowed to issue tokens to the given client and user. | `False` |
| `Authorize Scope`  | Boolean | This setting will pass the scope(s) that the client is requesting to be passed as resource attributes to the PDP. It is expected that the PDP will have a policy in place to determine of the scope(s) is/are allowed and if tokens should be issued.   | `False` |

### PDP Configuration
| Name         | Type   | Description                                                                                      | Example                   | Default                 |
|--------------|--------|--------------------------------------------------------------------------------------------------|---------------------------|-------------------------|
| `HttpClient` | String | The ID of the HttpClient that the Authorization Manager will use to call the OpenID AuthZEN PDP. | `authzen-http-client`     |                         |
| `PDP Host`   | String | The hostname of the OpenID AuthZEN PDP.                                                          | `authzen-pdp.example.com` | `localhost`             |
| `PDP Port`   | String | The port that the OpenID AuthZEN PDP is exposing its service on.                                 | `8443`                    | `443`                   |
| `PDP Path`   | String | The path of the OpenID AuthZEN PDP that accepts authorization requests.                          | `/pdp`                    | `/access/v1/evaluation` |

## Future Improvements

- Dynamic claims generation - Not yet supported by AuthZEN.
- Authorize if configured claims are allowed to be issued in token(s).
- Add authorization to all supported flows. Currently only Code Flow and Client Credentials are implemented.

## More Information

- Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server
- [OpenID AuthZEN Working Group)](https://openid.github.io/authzen/)
