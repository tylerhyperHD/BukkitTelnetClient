package me.StevenLawson.BukkitTelnetClient;

import java.util.*;
import org.w3c.dom.*;

public class ServerEntry
{
    private String name;
    private String address;
    private boolean lastUsed = false;
    private String plugin;

    public ServerEntry(final String name, final String address)
    {
        this.name = name;
        this.address = address;
    }

    public ServerEntry(final String name, final String address, final boolean lastUsed)
    {
        this.name = name;
        this.address = address;
        this.lastUsed = lastUsed;
    }
    
    public ServerEntry(final String name, final String address, final boolean lastUsed, final String pluginName)
    {
        this.name = name;
        this.address = address;
        this.lastUsed = lastUsed;
        this.plugin = pluginName;
    }


    public String getName()
    {
        return name;
    }
    
    public String getPlugin()
    {
        return plugin;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public boolean isLastUsed()
    {
        return lastUsed;
    }

    public void setLastUsed(boolean lastUsed)
    {
        this.lastUsed = lastUsed;
    }
    
    public void setPlugin(String plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.address);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        final ServerEntry other = (ServerEntry) obj;

        if (!Objects.equals(this.getName(), other.getName()))
        {
            return false;
        }

        if (!Objects.equals(this.getAddress(), other.getAddress()))
        {
            return false;
        }

        return true;
    }

    public static Element listToXML(final Set<ServerEntry> servers, final Document doc)
    {
        final Element serversElement = doc.createElement("servers");

        for (final ServerEntry command : servers)
        {
            final Element commandElement = doc.createElement("server");
            serversElement.appendChild(commandElement);

            final Element serverName = doc.createElement("name");
            serverName.appendChild(doc.createTextNode(command.getName()));
            commandElement.appendChild(serverName);

            final Element serverAddress = doc.createElement("address");
            serverAddress.appendChild(doc.createTextNode(command.getAddress()));
            commandElement.appendChild(serverAddress);

            final Element serverLastUsed = doc.createElement("lastUsed");
            serverLastUsed.appendChild(doc.createTextNode(Boolean.toString(command.isLastUsed())));
            commandElement.appendChild(serverLastUsed);
            
            final Element serverPluginName= doc.createElement("pluginName");
            serverPluginName.appendChild(doc.createTextNode(command.getPlugin()));
            commandElement.appendChild(serverPluginName);
        }

        return serversElement;
    }

    public static boolean xmlToList(final Set<ServerEntry> servers, final Document doc)
    {
        NodeList serverNodes = doc.getDocumentElement().getElementsByTagName("servers");
        if (serverNodes.getLength() < 1)
        {
            return false;
        }
        serverNodes = serverNodes.item(0).getChildNodes();

        servers.clear();

        for (int i = 0; i < serverNodes.getLength(); i++)
        {
            final Node node = serverNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                final Element element = (Element) node;

                final ServerEntry server = new ServerEntry(
                        element.getElementsByTagName("name").item(0).getTextContent(),
                        element.getElementsByTagName("address").item(0).getTextContent(),
                        Boolean.valueOf(element.getElementsByTagName("lastUsed").item(0).getTextContent()),
                        element.getElementsByTagName("pluginName").item(0).getTextContent()
                );

                servers.add(server);
            }
        }

        return true;
    }

    @Override
    public String toString()
    {
        return String.format("%s (%s)", this.name, this.address);
    }
}
