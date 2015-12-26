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

import com.sun.jna.NativeLibrary;

/**
 *
 * @author scream3r
 */
public class SerialPort {

    private SerialNativeInterface serialInterface;
    private SerialPortEventListener eventListener;
    private int portHandle;
    private String portName;
    private boolean portOpened = false;
    private boolean maskAssigned = false;
    private boolean eventListenerAdded = false;
    
    
    public static final int BAUDRATE_110 = 110;
    public static final int BAUDRATE_300 = 300;
    public static final int BAUDRATE_600 = 600;
    public static final int BAUDRATE_1200 = 1200;
    public static final int BAUDRATE_4800 = 4800;
    public static final int BAUDRATE_9600 = 9600;
    public static final int BAUDRATE_14400 = 14400;
    public static final int BAUDRATE_19200 = 19200;
    public static final int BAUDRATE_38400 = 38400;
    public static final int BAUDRATE_57600 = 57600;
    public static final int BAUDRATE_115200 = 115200;
    public static final int BAUDRATE_128000 = 128000;
    public static final int BAUDRATE_256000 = 256000;


    public static final int DATABITS_5 = 5;
    public static final int DATABITS_6 = 6;
    public static final int DATABITS_7 = 7;
    public static final int DATABITS_8 = 8;
    

    public static final int STOPBITS_1 = 1;
    public static final int STOPBITS_2 = 2;
    public static final int STOPBITS_1_5 = 3;
    

    public static final int PARITY_NONE = 0;
    public static final int PARITY_ODD = 1;
    public static final int PARITY_EVEN = 2;
    public static final int PARITY_MARK = 3;
    public static final int PARITY_SPACE = 4;
     

    public static final int PURGE_RXABORT = 0x0002;
    public static final int PURGE_RXCLEAR = 0x0008;
    public static final int PURGE_TXABORT = 0x0001;
    public static final int PURGE_TXCLEAR = 0x0004;


    public static final int MASK_RXCHAR = 1;
    public static final int MASK_RXFLAG = 2;
    public static final int MASK_TXEMPTY = 4;
    public static final int MASK_CTS = 8;
    public static final int MASK_DSR = 16;
    public static final int MASK_RLSD = 32;
    public static final int MASK_BREAK = 64;
    public static final int MASK_ERR = 128;
    public static final int MASK_RING = 256;

    public static String SERIAL_LIBRARY_SEARCH_PATH = "/SerialLibrary/Win32/";
    
    public SerialPort(String portName) {
        
        this.portName = portName;
        serialInterface = new SerialNativeInterface();
    }

    /**
     * Получение имени порта с которым ведётся работа.
     *
     * @return Метод возвращает имя порта, с которым ведётся работа, в виде строки.
     */
    public String getPortName(){
        return portName;
    }

    /**
     * Получение статуса порта.
     * 
     * @return Метод возвращает true если порт открыт и false в противном случае.
     */
    public boolean isOpened() {
        return portOpened;
    }

    /**
     * Открытие порта.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     *
     * @throws SerialPortException
     */
    public boolean openPort() throws SerialPortException {
        if(portOpened){
            throw new SerialPortException(portName, "openPort()", SerialPortException.TYPE_PORT_ALREADY_OPENED);
        }
        portHandle = serialInterface.openPort(portName);
        if(portHandle != -1){
            portOpened = true;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Установка параметров порта.
     * @param baudRate скорость передачи данных.
     * @param dataBits биты данных
     * @param stopBits стоповые биты
     * @param parity четность
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     *
     * @throws SerialPortException
     */
    public boolean setParams(int baudRate, int dataBits, int stopBits, int parity) throws SerialPortException {
        checkPortOpened("setParams()");
        if(stopBits == 1){
            stopBits = 0;
        }
        else if(stopBits == 3){
            stopBits = 1;
        }
        return serialInterface.setParams(portHandle, baudRate, dataBits, stopBits, parity);
    }

    /**
     * Очистка входного и выходного буфера. На вход необходимо подать нужные
     * флаги. В качестве флагов используйте переменные с префиксом
     * <b>"PURGE_"</b>, например <b>"PURGE_RXCLEAR"</b>. Передаваемый параметр
     * flags - адитивная величина, т.о. допускается сложение флагов. Например,
     * если необходимо очистить входящий и исходящий буфер, для этого можно
     * передать в метод следующий параметр <b>"PURGE_RXCLEAR + PURGE_TXCLEAR"</b>.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в противном
     * случае false.
     *
     * @throws SerialPortException
     */
    public boolean purgePort(int flags) throws SerialPortException {
        checkPortOpened("purgePort()");
        return serialInterface.purgePort(portHandle, flags);
    }

    /**
     * Установка маски ивентов. На вход необходимо подать нужные флаги. В качестве
     * флагов используйте переменные с префиксом <b>"MASK_"</b>, например 
     * <b>"MASK_RXCHAR"</b>. Передаваемый параметр mask - адитивная величина,
     * т.о. допускается сложение флагов. Например, если нужно получать сообщения
     * о приходе данных и изменении статусов CTS и DSR, то необходимо задать
     * маску вида - <b>"MASK_RXCHAR + MASK_CTS + MASK_DSR"</b>.
     * 
     * @return Если операция удачно выполнена, то метод вернёт true, в
     * противном случае false.
     *
     * @throws SerialPortException
     */
    public boolean setEventsMask(int mask) throws SerialPortException {
        checkPortOpened("setEventsMask()");
        boolean returnValue = serialInterface.setEventsMask(portHandle, mask);
        if(!returnValue){
            throw new SerialPortException(portName, "setEventsMask()", SerialPortException.TYPE_CANT_SET_MASK);
        }
        if(mask > 0){
            maskAssigned = true;
        }
        else {
            maskAssigned = false;
        }
        return returnValue;
    }

    /**
     * Получение маски ивентов для порта.
     * 
     * @return Метод возвращает маску ивентов в виде переменной типа int. Данная
     * переменная является адитивной величиной.
     *
     * @throws SerialPortException
     */
    public int getEventsMask() throws SerialPortException {
        checkPortOpened("getEventsMask()");
        return serialInterface.getEventsMask(portHandle);
    }

    /**
     * Изменение состояния линии RTS. Передайте true для включения линии RTS и 
     * false для её выключения.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в
     * противном случае false.
     *
     * @throws SerialPortException
     */
    public boolean setRTS(boolean enabled) throws SerialPortException {
        checkPortOpened("setRTS()");
        return serialInterface.setRTS(portHandle, enabled);
    }

    /**
     * Изменение состояния линии DTR. Передайте true для включения линии DTR и
     * false для её выключения.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в
     * противном случае false.
     *
     * @throws SerialPortException
     */
    public boolean setDTR(boolean enabled) throws SerialPortException {
        checkPortOpened("setDTR()");
        return serialInterface.setDTR(portHandle, enabled);
    }

    /**
     * Запись данных в порт. На вход подаётся массив байт.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в
     * противном случае false.
     * 
     * @throws SerialPortException
     */
    public boolean writeBytes(byte[] buffer) throws SerialPortException {
        checkPortOpened("writeBytes()");
        return serialInterface.writeBytes(portHandle, buffer);
    }

    /**
     * Чтение данных из порта. На вход подаётся переменная <b>"byteCount"</b> 
     * указывающая на количество байт, которые будут прочитаны из буфера порта. 
     *
     * @return Метод возвращает массив прочитанных байт.
     *
     * @throws SerialPortException
     */
    public byte[] readBytes(int byteCount) throws SerialPortException {
        checkPortOpened("readBytes()");
        return serialInterface.readBytes(portHandle, byteCount);
    }

    private int[][] waitEvents() {
        return serialInterface.waitEvents(portHandle);
    }

    private void checkPortOpened(String methodName) throws SerialPortException {
        if(!portOpened){
            throw new SerialPortException("EMPTY", methodName, SerialPortException.TYPE_PORT_NOT_OPENED);
        }
    }

    /**
     * Получение статуса линий. Статус линий передаётся в виде 0 - выкл. и 1 - вкл.
     *
     * @return Метод возвращает массив содержащий информацию о линиях в следующем порядке:
     * <br><b>элемент 0</b> - статус линии <b>CTS</b></br>
     * <br><b>элемент 1</b> - статус линии <b>DSR</b></br>
     * <br><b>элемент 2</b> - статус линии <b>RING</b></br>
     * <br><b>элемент 3</b> - статус линии <b>RLSD</b></br>
     *
     * @throws SerialPortException
     */
    public int[] getLinesStatus() throws SerialPortException {
        checkPortOpened("getLinesStatus()");
        return serialInterface.getLinesStatus(portHandle);
    }

    /**
     * Получение статуса линии CTS.
     *
     * @return Если линия CTS активна метод вернёт true, в противном случае false.
     *
     * @throws SerialPortException
     */
    public boolean isCTS() throws SerialPortException {
        checkPortOpened("isCTS()");
        if(serialInterface.getLinesStatus(portHandle)[0] == 1){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Получение статуса линии DSR.
     *
     * @return Если линия DSR активна метод вернёт true, в противном случае false.
     *
     * @throws SerialPortException
     */
    public boolean isDSR() throws SerialPortException {
        checkPortOpened("isDSR()");
        if(serialInterface.getLinesStatus(portHandle)[1] == 1){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Получение статуса линии RING.
     * 
     * @return Если линия RING активна метод вернёт true, в противном случае false.
     *
     * @throws SerialPortException
     */
    public boolean isRING() throws SerialPortException {
        checkPortOpened("isRING()");
        if(serialInterface.getLinesStatus(portHandle)[2] == 1){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Получение статуса линии RLSD.
     * 
     * @return Если линия RLSD активна метод вернёт true, в противном случае false.
     *
     * @throws SerialPortException
     */
    public boolean isRLSD() throws SerialPortException {
        checkPortOpened("isRLSD()");
        if(serialInterface.getLinesStatus(portHandle)[3] == 1){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Добавление обработчика событий. Методу необходимо передать объект типа
     * <b>"SerialPortEventListener"</b>. Данный объект должен быть должным образом
     * описан, т.к. именно он будет отвечать за обработку произошедших событий. 
     * Данный метод самостоятельно установит маску в состояние <b>"MASK_RXCHAR"</b>
     * если она не была установлена заранее.
     *
     * @throws SerialPortException
     */
    public void addEventListener(SerialPortEventListener listener) throws SerialPortException {
        checkPortOpened("addEventListener()");
        if(!eventListenerAdded){
            if(maskAssigned){
                eventListener = listener;
                eventThread = new EventThread();
                eventThread.setName("EventThread " + portName);
                eventThread.start();
                eventListenerAdded = true;
            }
            else {
                setEventsMask(MASK_RXCHAR);
                eventListener = listener;
                eventThread = new EventThread();
                eventThread.setName("EventThread " + portName);
                eventThread.start();
                eventListenerAdded = true;
            }
        }
        else {
            throw new SerialPortException(portName, "addEventListener()", SerialPortException.TYPE_LISTENER_ALREADY_ADDED);
        }
    }

    /**
     * Добавление обработчика событий. Методу необходимо передать объект типа
     * <b>"SerialPortEventListener"</b>. Данный объект должен быть должным образом
     * описан, т.к. именно он будет отвечать за обработку произошедших событий. 
     * Также данному методу необходимо передать маску ивентов, для этого используйте
     * переменные с префиксом <b>"MASK_"</b> например <b>"MASK_RXCHAR"</b>.
     *
     * @see #setEventsMask(int) setEventsMask(int mask)
     *
     * @throws SerialPortException
     */
    public void addEventListener(SerialPortEventListener listener, int mask) throws SerialPortException {
        checkPortOpened("addEventListener()");
        if(!eventListenerAdded){
            setEventsMask(mask);
            eventListener = listener;
            eventThread = new EventThread();
            eventThread.setName("EventThread " + portName);
            eventThread.start();
            eventListenerAdded = true;
        }
        else {
            throw new SerialPortException(portName, "addEventListener()", SerialPortException.TYPE_LISTENER_ALREADY_ADDED);
        }
    }

    /**
     * Удаление обработчика событий. При этом маска выставляется в значение 0. Т.о.
     * при следующем добавлении обработчика событий Вам потребуется снова установить
     * необходимую маску ивентов.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в
     * противном случае false.
     * 
     * @throws SerialPortException
     */
    public boolean removeEventListener() throws SerialPortException {
        checkPortOpened("removeEventListener()");
        if(!eventListenerAdded){
            throw new SerialPortException(portName, "removeEventListener()", SerialPortException.TYPE_CANT_REMOVE_LISTENER);
        }
        eventThread.threadTerminated = true;
        setEventsMask(0);
        if(Thread.currentThread().getId() != eventThread.getId()){
            if(eventThread.isAlive()){
                try {
                    eventThread.join(5000);
                }
                catch (InterruptedException ex) {
                    throw new SerialPortException(portName, "removeEventListener()", SerialPortException.TYPE_LISTENER_THREAD_INTERRUPTED);
                }
            }
        }
        eventListenerAdded = false;
        return true;
    }

    /**
     * Закрытие порта. Данный метод сначала удаляет обработчик событий,
     * затем закрывает порт.
     *
     * @return Если операция удачно выполнена, то метод вернёт true, в
     * противном случае false.
     * 
     * @throws SerialPortException
     */
    public boolean closePort() throws SerialPortException {
        checkPortOpened("closePort()");
        if(eventListenerAdded){
            removeEventListener();
        }
        boolean returnValue = serialInterface.closePort(portHandle);
        if(returnValue){
            maskAssigned = false;
            portOpened = false;
        }
        return returnValue;
    }

    private EventThread eventThread;

    private class EventThread extends Thread {

        private boolean threadTerminated = false;
        
        @Override
        public void run() {
            while(!threadTerminated){
                int[][] eventArray = waitEvents();
                for(int i = 0; i < eventArray.length; i++){
                    if(eventArray[i][0] > 0 && !threadTerminated){
                        eventListener.serialEvent(new SerialPortEvent(portName, eventArray[i][0], eventArray[i][1]));
                    }
                }
            }
        }
    }
}
