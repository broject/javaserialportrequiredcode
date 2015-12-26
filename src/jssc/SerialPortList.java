/* jSSC (Java Simple Serial Connector) - serial port communication library.
 * © Alexey Sokolov (scream3r), 2010.
 *
 * This file is part of jSSC.
 *
 * jSSC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jSSC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSSC.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you use jSSC in public project you can inform me about this by e-mail,
 * of course if you want it.
 *
 * e-mail: scream3r.org@gmail.com
 * web-site: www.scream3r.org
 */
package jssc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author scream3r
 */
public class SerialPortList {

    private static SerialNativeInterface serialInterface;
    private static Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String valueA, String valueB) {
            int result = 0;
            if(valueA.toLowerCase().contains("com") && valueB.toLowerCase().contains("com")){
                try {
                    int index1 = Integer.valueOf(valueA.toLowerCase().replace("com", ""));
                    int index2 = Integer.valueOf(valueB.toLowerCase().replace("com", ""));
                    result = index1 - index2;
                }
                catch (Exception ex) {
                    result = valueA.compareToIgnoreCase(valueB);
                }
            } 
            else {
                result = valueA.compareToIgnoreCase(valueB);
            }
            return result;
        }
    };

    static {
        serialInterface = new SerialNativeInterface();
    }

    /**
     * Получение списка последовательных портов в системе в упорядоченном виде.
     */
    public static String[] getPortNames() {
        String[] portNames = serialInterface.getSerialPortNames();
        TreeSet<String> ports = new TreeSet<String>(comparator);
        ports.addAll(Arrays.asList(portNames));
        return ports.toArray(new String[ports.size()]);
    }
}
