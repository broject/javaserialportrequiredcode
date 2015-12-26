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
public class SerialPortEvent {

    private String portName;
    private int eventType;
    private int eventValue;

    public static final int RXCHAR = 1;
    public static final int RXFLAG = 2;
    public static final int TXEMPTY = 4;
    public static final int CTS = 8;
    public static final int DSR = 16;
    public static final int RLSD = 32;
    public static final int BREAK = 64;
    public static final int ERR = 128;
    public static final int RING = 256;

    public SerialPortEvent(String portName, int eventType, int eventValue){
        this.portName = portName;
        this.eventType = eventType;
        this.eventValue = eventValue;
    }

    /**
     * Получение имени порта от которого пришёл ивент.
     */
    public String getPortName() {
        return portName;
    }

    /**
     * Получение типа ивента.
     */
    public int getEventType() {
        return eventType;
    }

    /**
     * Получение значения ивента.
     * <br></br>
     * <br><u><b>Значения ивентов в зависимости от их типов:</b></u></br>
     * <br><b>RXCHAR</b> - количество байт во входящем буфере.</br>
     * <br><b>RXFLAG</b> - количество байт во входящем буфере.</br>
     * <br><b>TXEMPTY</b> - количество байт в исходящем буфере.</br>
     * <br><b>CTS</b> - состояние линии CTS (0 - выкл., 1 - вкл.).</br>
     * <br><b>DSR</b> - состояние линии DSR (0 - выкл., 1 - вкл.).</br>
     * <br><b>RLSD</b> - состояние линии RLSD (0 - выкл., 1 - вкл.).</br>
     * <br><b>BREAK</b> - 0.</br>
     * <br><b>RING</b> - состояние линии RING (0 - выкл., 1 - вкл.).</br>
     * 
     */
    public int getEventValue() {
        return eventValue;
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"RXCHAR"</b> и false в противном случае.
     */
    public boolean isRXCHAR() {
        if(eventType == RXCHAR){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"RXFLAG"</b> и false в противном случае.
     */
    public boolean isRXFLAG() {
        if(eventType == RXFLAG){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"TXEMPTY"</b> и false в противном случае.
     */
    public boolean isTXEMPTY() {
        if(eventType == TXEMPTY){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"CTS"</b> и false в противном случае.
     */
    public boolean isCTS() {
        if(eventType == CTS){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"DSR"</b> и false в противном случае.
     */
    public boolean isDSR() {
        if(eventType == DSR){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"RLSD"</b> и false в противном случае.
     */
    public boolean isRLSD() {
        if(eventType == RLSD){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"BREAK"</b> и false в противном случае.
     */
    public boolean isBREAK() {
        if(eventType == BREAK){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"ERR"</b> и false в противном случае.
     */
    public boolean isERR() {
        if(eventType == ERR){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Метод возвращает true если получен ивент типа
     * <b>"RING"</b> и false в противном случае.
     */
    public boolean isRING() {
        if(eventType == RING){
            return true;
        }
        else {
            return false;
        }
    }
}
