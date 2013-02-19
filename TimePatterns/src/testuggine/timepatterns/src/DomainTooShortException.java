package testuggine.timepatterns.src;

public class DomainTooShortException extends Exception  
{  
	
	private static final long serialVersionUID = 8305356519333443542L;

	public DomainTooShortException(String message)          
    {   
        super(message);         
    }       
}
