#include <windows.h>
#include "board.h"
#include "AI.h"
#include "human.h"
#include "resources.h"
/* This is where all the input to the window goes to */
board myBoard;
AI AIplayer(&myBoard);
human humanPlayer(&myBoard);
LRESULT CALLBACK WndProc(HWND hwnd, UINT Message, WPARAM wParam, LPARAM lParam) {
	
	switch(Message) {
		HDC hdc;
		PAINTSTRUCT ps;
		
		case WM_LBUTTONDOWN:		//¼ì²âÊó±êµÄµã»÷ÏûÏ¢ 
		{
			hdc=GetDC(hwnd);		//»ñÈ¡´°¿Ú¾ä±ú 
			int winner;
			int x = LOWORD (lParam);
			int y = HIWORD (lParam);		//»ñÈ¡Êó±êµã»÷µÄÎ»ÖÃ 
			if(myBoard.runState) 			//Æå¾Ö´¦ÓÚÕý³£ÏÂÆå×´Ì¬ 
			{
				humanPlayer.putChess(x,y);		//ÈË¸ù¾Ý×ø±êÂä×Ó 
				myBoard.darwBroad(hdc);			//ÖØ»­ÆåÅÌ 
				winner = myBoard.checkBoard(humanPlayer.first);			//ÅÐ¶ÏÓÎÏ·ÊÇ·ñ½áÊø 
				if(winner > 0) MessageBox(hwnd,"  ÄãÓ®À²£¡£¡£¡","ÓÎÏ·½áÊø",MB_ICONWARNING|MB_OK);
												//½áÊøÔòÊä³öÌáÊ¾ÐÅÏ¢ 
			}
			
			if(myBoard.runState)			//Æå¾Ö´¦ÓÚÕý³£ÏÂÆå×´Ì¬ 
			{
				AIplayer.putChess();		//AIÂä×Ó 
				myBoard.darwBroad(hdc,AIplayer.chooseX, AIplayer.chooseY);		//ÖØ»­ÆåÅÌ 
				winner = myBoard.checkBoard(AIplayer.first);			//ÅÐ¶ÏÓÎÏ·ÊÇ·ñ½áÊø 
				if(winner > 0) MessageBox(hwnd,"  ÄãÊä¿©£¡£¡£¡","ÓÎÏ·½áÊø",MB_ICONWARNING|MB_OK);
												//½áÊøÔòÊä³öÌáÊ¾ÐÅÏ¢ 
			} 
			
			if(!myBoard.runState) 			//Èç¹ûÆå¾Ö×´Ì¬ÒÑ¾­½áÊø 
			{
				if(MessageBox(hwnd,"ÄãÒªÔÙÀ´Ò»´ÎÂð£¿","ÔÙÀ´Ò»´Î",MB_ICONQUESTION|MB_YESNO) == IDYES)
				{							//Ñ¯ÎÊÊÇ·ñÔÙÀ´Ò»´Î 
					myBoard.reset();			//ÊÇµÄ»°ÔòÖØÖÃÆåÅÌ 
					SendMessage(hwnd,WM_PAINT,0,0);		//·¢ËÍÖØ»­ÆåÅÌµÄÏûÏ¢ 
				}
				else SendMessage(hwnd,WM_DESTROY,0,0);		//·ñµÄ»°·¢ËÍÏú»Ù´°¿ÚµÄÏûÏ¢ 
				
			}
			ReleaseDC(hwnd,hdc);		//ÊÍ·Å´°¿Ú¾ä±ú 
			return 0;
		} 
		case WM_PAINT:					
		{
			InvalidateRect (hwnd, NULL, TRUE);
			hdc = BeginPaint (hwnd, &ps);
			myBoard.darwBroad(hdc);			//»­³ö³õÊ¼ÆåÅÌ 
			if(MessageBox(hwnd,"ÇëÑ¡ÔñÊÇ·ñÏÈÊÖ£¨Ö´ºÚ×Ó£©","Ñ¡ÔñÏÈÊÖ",MB_ICONQUESTION|MB_YESNO) == IDYES)
			{									//Ñ¯ÎÊÊÇ·ñÏÈÊÖ 
				myBoard.now = true;				//ÏÈÊÖ¾Í½«µ±Ç°Âä×Ó×´Ì¬ÉèÎªtrue 
				humanPlayer.first = 1;			//ÈËÀàµÄfirstÉèÎª 1
				AIplayer.first = -1;			//AIµÄfirstÉèÎª-1 
			}
			else 
			{
				myBoard.now = false;		//ºóÊÖÔò½« 
				humanPlayer.first = -1;
				AIplayer.first = 1;
				AIplayer.putChess();
				myBoard.darwBroad(hdc,AIplayer.chooseX, AIplayer.chooseY);
			}
			EndPaint (hwnd, &ps);
			return 0;
		}
		
		/* Upon destruction, tell the main thread to stop */
		case WM_DESTROY: {
			PostQuitMessage(0);
			break;
		}
		
		/* All other messages (a lot of them) are processed using default procedures */
		default:
			return DefWindowProc(hwnd, Message, wParam, lParam);
	}
	return 0;
}

/* The 'main' function of Win32 GUI programs: this is where execution starts */
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
	WNDCLASSEX wc; /* A properties struct of our window */
	HWND hwnd; /* A 'HANDLE', hence the H, or a pointer to our window */
	MSG msg; /* A temporary location for all messages */

	/* zero out the struct and set the stuff we want to modify */
	memset(&wc,0,sizeof(wc));
	wc.cbSize		 = sizeof(WNDCLASSEX);
	wc.lpfnWndProc	 = WndProc; /* This is where we will send messages to */
	wc.hInstance	 = hInstance;
	wc.hCursor		 = LoadCursor(NULL, IDC_ARROW);
	
	/* White, COLOR_WINDOW is just a #define for a system color, try Ctrl+Clicking it */
	wc.hbrBackground = (HBRUSH)CreateSolidBrush(RGB(255,200,30)); 
	wc.lpszClassName = "WindowClass";
	wc.hIcon		 = NULL; /* Load a standard icon */
	wc.hIconSm		 = LoadIcon(hInstance, "A"); /* use the name "A" to use the project icon */

	if(!RegisterClassEx(&wc)) {
		MessageBox(NULL, "Window Registration Failed!","Error!",MB_ICONEXCLAMATION|MB_OK);
		return 0;
	}

	hwnd = CreateWindowEx(WS_EX_CLIENTEDGE,"WindowClass","Îå×ÓÆå",WS_VISIBLE|WS_SYSMENU,
		CW_USEDEFAULT, /* x */
		CW_USEDEFAULT, /* y */
		630, /* width */
		650, /* height */
		NULL,NULL,hInstance,NULL);

	if(hwnd == NULL) {
		MessageBox(NULL, "Window Creation Failed!","Error!",MB_ICONEXCLAMATION|MB_OK);
		return 0;
	}

	/*
		This is the heart of our program where all input is processed and 
		sent to WndProc. Note that GetMessage blocks code flow until it receives something, so
		this loop will not produce unreasonably high CPU usage
	*/
	while(GetMessage(&msg, NULL, 0, 0) > 0) { /* If no error is received... */
		TranslateMessage(&msg); /* Translate key codes to chars if present */
		DispatchMessage(&msg); /* Send it to WndProc */
	}
	return msg.wParam;
}
