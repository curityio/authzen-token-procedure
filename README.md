# AuthZEN Token Procedure Plugin
[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

A custom Token Procedure plugin for the Curity Identity Server.

The plugin supports configuration for different use cases:
- If token is allowed to be issued in general (this is always on if the token procedure is configured)
- If requested scope(s) is/are allowed
- If token is allowed to be issued for the given client/app

> [!NOTE]
> Additional details are outlined in the [OpenID AuthZEN Token Procedure](https://curity.io/resources/learn/authzen-token-procedure/) article.
## Building the Plugin

You can build the plugin by issuing the command ``mvn package``. This will produce a JAR file in the ``target`` directory,
which can be installed.

## Installing the Plugin

To install the plugin, copy the compiled JAR (and all of its dependencies) from the ``target`` directory into the :file:`${IDSVR_HOME}/usr/share/plugins/authzen-token-procedure`
on each node, including the admin node. For more information about installing plugins, refer to [curity.io/plugins](https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html#plugin-installation).

## Required Dependencies

For a list of the dependencies and their versions, run ``mvn dependency:list``. Ensure that all of these are installed in
the plugin group; otherwise, they will not be accessible to this plugin and runtime errors will result.

## Configuration

### Authorization Configuration
| Parameter          | Type    | Description                                                                                               | Default |
|--------------------|---------|-----------------------------------------------------------------------------------------------------------|---------|
| `Authorize Client` | Boolean | When enabled, the `client_id` of the request is passed as a resource attribute in the request to the PDP. | `False` |
| `Authorize Scope`  | Boolean | When enabled, the scope(s) requested are passed as resource attributes to the PDP.                        | `False` |

### PDP Configuration
| Name         | Type   | Description                                                                                 | Example                   | Default                 |
|--------------|--------|---------------------------------------------------------------------------------------------|---------------------------|-------------------------|
| `HttpClient` | String | The ID of the HttpClient that the Authorization Manager use to call the OpenID AuthZEN PDP. | `authzen-http-client`     |                         |
| `PDP Host`   | String | The hostname of the OpenID AuthZEN PDP.                                                     | `authzen-pdp.example.com` | `localhost`             |
| `PDP Port`   | String | The port that the OpenID AuthZEN PDP is exposing its service on.                            | `8443`                    | `443`                   |
| `PDP Path`   | String | The path of the OpenID AuthZEN PDP that accepts authorization requests.                     | `/pdp`                    | `/access/v1/evaluation` |

## Future Improvements

- Dynamic claims generation - Not yet supported by AuthZEN.
- Authorize if configured claims are allowed to be issued in token(s).
- Add authorization to all supported flows. Currently only Code Flow and Client Credentials are implemented.

## More Information

- Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server
- [OpenID AuthZEN Working Group)](https://openid.github.io/authzen/)
