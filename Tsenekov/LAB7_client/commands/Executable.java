package commands;



/**
 * Интерфейс для всех выполняемых команд.
 * @author dim0n4eg
 */
public interface Executable {
  /**
   * Выполнить что-либо.
   *
   * @param arguments Аргумент для выполнения
   * @return результат выполнения
   */
  boolean apply(String[] arguments);
}
