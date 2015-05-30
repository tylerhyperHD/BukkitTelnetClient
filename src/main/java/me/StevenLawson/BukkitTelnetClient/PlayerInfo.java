package me.StevenLawson.BukkitTelnetClient;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class PlayerInfo
{
    public static int numColumns;
    public static String[] columnNames;

    private final String name;
    private final String ip;
    private final String displayName;
    private final String uuid;

    private final boolean admin;
    private final boolean superAdmin;
    private final boolean seniorAdmin;
    private final boolean executiveAdmin;
    private final boolean specialistAdmin;
    private final boolean systemAdmin;
    private final boolean efmCreator;
    private final boolean owner;
    private final boolean overlord;
    private final String tag;
    private final String nickName;

    static
    {
        final Map<Integer, String> columnNamesMap = new HashMap<>();

        int _numColumns = 0;
        final Method[] declaredMethods = PlayerInfo.class.getDeclaredMethods();
        for (final Method method : declaredMethods)
        {
            final Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (final Annotation annotation : declaredAnnotations)
            {
                if (annotation instanceof PlayerTableColumn)
                {
                    PlayerTableColumn playerInfoTag = (PlayerTableColumn) annotation;
                    columnNamesMap.put(playerInfoTag.column(), playerInfoTag.name());
                    _numColumns++;
                }
            }
        }

        final String[] _columnNames = new String[_numColumns];
        for (int i = 0; i < _numColumns; i++)
        {
            _columnNames[i] = columnNamesMap.get(i);
        }

        columnNames = _columnNames;
        numColumns = _numColumns;
    }

    public PlayerInfo(String name, String ip, String displayName, String uuid, boolean admin, boolean superAdmin, boolean seniorAdmin, boolean executiveAdmin, boolean specialistAdmin, boolean systemAdmin, boolean efmCreator, boolean owner, boolean overlord, String tag, String nickName)
    {
        this.name = name;
        this.ip = ip;
        this.displayName = displayName;
        this.uuid = uuid;
        this.admin = admin;
        this.superAdmin = superAdmin;
        this.seniorAdmin = seniorAdmin;
        this.executiveAdmin = executiveAdmin;
        this.specialistAdmin = specialistAdmin;
        this.systemAdmin = systemAdmin;
        this.efmCreator = efmCreator;
        this.owner = owner;
        this.overlord = overlord;
        this.tag = tag;
        this.nickName = nickName;
    }

    @PlayerTableColumn(name = "Name", column = 0)
    public String getName()
    {
        return name;
    }

    @PlayerTableColumn(name = "IP", column = 1)
    public String getIp()
    {
        return ip;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getUuid()
    {
        return uuid;
    }

    public boolean isAdmin()
    {
        return admin;
    }

    public boolean isSuper()
    {
        return superAdmin;
    }
    
    public boolean isSenior()
    {
        return seniorAdmin;
    }
    
    public boolean isExecutive()
    {
        return executiveAdmin;
    }
    
    public boolean isSpecialist()
    {
        return specialistAdmin;
    }
    public boolean isSystem()
    {
        return systemAdmin;
    }
    public boolean isEfmcreator()
    {
        return efmCreator;
    }
    public boolean isOwner()
    {
        return owner;
    }
    
    public boolean isOverlord()
    {
        return overlord;
    }

    @PlayerTableColumn(name = "Tag", column = 2)
    public String getTag()
    {
        return tag == null || tag.isEmpty() || tag.equalsIgnoreCase("null") ? "" : tag;
    }

    @PlayerTableColumn(name = "Nickname", column = 3)
    public String getNickName()
    {
        return nickName == null || nickName.isEmpty() || nickName.equalsIgnoreCase("null") ? "" : nickName;
    }

    @PlayerTableColumn(name = "Admin Level", column = 4)
    public String getAdminLevel()
    {
        if (isAdmin())
        {
            if (isOverlord())
            {
                return "Overlord";
            }
            else if (isOwner())
            {
                return "Owner";
            }
            else if (isSystem())
            {
                return "System";
            }
            else if (isSpecialist())
            {
                return "Specialist";
            }
            else if (isExecutive())
            {
                return "Executive";
            }
            else if (isSenior())
            {
                return "Senior";
            }
            else if (isSuper())
            {
                return "Reg Admin";
            }
            else
            {
                return "Admin";
            }
        }
        return "";
    }

    public String getColumnValue(int columnIndex)
    {
        final Method[] declaredMethods = this.getClass().getDeclaredMethods();
        for (final Method method : declaredMethods)
        {
            final Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (final Annotation annotation : declaredAnnotations)
            {
                if (annotation instanceof PlayerTableColumn)
                {
                    PlayerTableColumn playerInfoTag = (PlayerTableColumn) annotation;

                    if (playerInfoTag.column() == columnIndex)
                    {
                        try
                        {
                            final Object value = method.invoke(this);
                            if (value != null)
                            {
                                return value.toString();
                            }
                        }
                        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
                        {
                            BukkitTelnetClient.LOGGER.log(Level.SEVERE, null, ex);
                        }

                        return "null";
                    }
                }
            }
        }

        return "null";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PlayerTableColumn
    {
        public String name();

        public int column();
    }

    public static Comparator<PlayerInfo> getComparator()
    {
        return new Comparator<PlayerInfo>()
        {
            @Override
            public int compare(PlayerInfo a, PlayerInfo b)
            {
                return a.getName().compareTo(b.getName());
            }
        };
    }
}
