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

/**
 *
 * @author scream3r
 */
public class SerialNativeInterface {
    
    static {
        System.loadLibrary(SerialPort.SERIAL_LIBRARY_SEARCH_PATH + "libjssc");
    }

    /**
     * Открытие порта. В метод нужно передать имя порта который нужно открыть.
     * 
     * @return Метод возвращает <b>handle</b> открытого порта или <b>-1</b>
     * если порт не удалось открыть.
     */
    public native int openPort(String portName);

    /**
     * Установка параметров для открытого порта.
     *
     * @param handle handle открытого порта.
     * @param baudRate скорость передачи данных.
     * @param dataBits количество бит данных.
     * @param stopBits количество стоповых бит.
     * @param parity чётность.
     * 
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     */
    public native boolean setParams(int handle, int baudRate, int dataBits, int stopBits, int parity);

    /**
     * Очистка входного и выходного буфера.
     * 
     * @param handle handle открытого порта.
     * @param flags флаги задающие необходимые действия для метода purgePort.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     */
    public native boolean purgePort(int handle, int flags);

    /**
     * Закрытие порта.
     * 
     * @param handle handle открытого порта.
     * 
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     */
    public native boolean closePort(int handle);

    /**
     * Установка маски ивентов.
     *
     * @param handle handle открытого порта.
     * @param mask маска ивентов.
     * 
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     */
    public native boolean setEventsMask(int handle, int mask);

    /**
     * Получение маски ивентов для порта.
     * 
     * @param handle handle открытого порта.
     * 
     * @return Метод возвращает маску ивентов в виде переменной типа <b>int</b>.
     */
    public native int getEventsMask(int handle);

    /**
     * Ожидание ивентов.
     *
     * @param handle handle открытого порта.
     *
     * @return Метод возвращает вдумерный массив, содержащий типы ивентов и их
     * значения (<b>events[i][0] - тип ивента</b>, <b>events[i][1] - значение ивента</b>).
     */
    public native int[][] waitEvents(int handle);

    /**
     * Изменение состояния линии RTS.
     * 
     * @param handle handle открытого порта.
     * @param value <b>true - вкл.</b>, <b>false - выкл.</b>
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     */
    public native boolean setRTS(int handle, boolean value);

    /**
     * Изменение состояния линии DTR.
     *
     * @param handle handle открытого порта.
     * @param value <b>true - вкл.</b>, <b>false - выкл.</b>
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     */
    public native boolean setDTR(int handle, boolean value);

    /**
     * Чтение данных из порта.
     * 
     * @param handle handle открытого порта.
     * @param byteCount количество байт которое необходимо прочитать.
     * 
     * @return Метод возвращает массив прочитанных байт.
     */
    public native byte[] readBytes(int handle, int byteCount);

    /**
     * Запись данных в порт.
     * 
     * @param handle handle открытого порта.
     * @param buffer массив байт для записи.
     * 
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     */
    public native boolean writeBytes(int handle, byte[] buffer);

    /**
     * Получение списка последовательных портов в системе в неупорядоченном виде.
     *
     * @return Метод возвращает список портов в виде массива строк.
     */
    public native String[] getSerialPortNames();

    /**
     * Получение статуса линий.
     * 
     * @param handle handle открытого порта.
     *
     * @return Метод возвращает массив содержащий информацию о линиях в следующем порядке:
     * <br><b>элемент 0</b> - статус линии <b>CTS</b></br>
     * <br><b>элемент 1</b> - статус линии <b>DSR</b></br>
     * <br><b>элемент 2</b> - статус линии <b>RING</b></br>
     * <br><b>элемент 3</b> - статус линии <b>RLSD</b></br>
     */
    public native int[] getLinesStatus(int handle);
}
