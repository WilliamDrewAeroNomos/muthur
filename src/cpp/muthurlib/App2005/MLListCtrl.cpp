//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "stdafx.h"
#include "mllistctrl.h"

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------
#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	MAPS
//------------------------------------------------------------------------------

BEGIN_MESSAGE_MAP(CMLListCtrl, CListCtrl)
	//{{AFX_MSG_MAP(CXmlListCtrl)
	ON_NOTIFY_REFLECT(LVN_GETDISPINFO, OnGetDispInfo)
	ON_NOTIFY_REFLECT_EX(LVN_ITEMCHANGED, OnItemChanged)
	ON_NOTIFY_REFLECT_EX(LVN_ITEMCHANGING, OnItemChanging)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlData::CMLListCtrlData()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Constructor
//
//------------------------------------------------------------------------------
CMLListCtrlData::CMLListCtrlData()
{
	m_bOwner = false;

	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_OBJECT_OWNED; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
}

CString CMLListCtrlData::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_OBJECT_OWNED:
		strName = "Owned:";
		break;	
	}

	return strName;
}

CString CMLListCtrlData::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_OBJECT_OWNED:
		strValue = m_bOwner ? "TRUE" : "FALSE";
		break;	
	}

	return strValue;
}

#include "stdafx.h"
#include "DVAircraft.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif



//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElement::CMLListCtrlElement()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Constructor
//
//------------------------------------------------------------------------------
CMLListCtrlElement::CMLListCtrlElement(CMLListCtrlData* pData)
{
	SetData(pData);
	m_iOwner = 0;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElement::~CMLListCtrlElement()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Destructor
//
//------------------------------------------------------------------------------
CMLListCtrlElement::~CMLListCtrlElement()
{
}

CMLListCtrlColumns* CMLListCtrlElement::GetListCtrlColumns()
{
	CMLListCtrlColumns*	pColumns = new CMLListCtrlColumns();
	CMLListCtrlColumn*	pColumn = NULL;

	pColumn = new CMLListCtrlColumn();
	pColumn->m_iIndex = 0;
	pColumn->m_strName = "Name";
	pColumns->Add(pColumn);

	pColumn = new CMLListCtrlColumn();
	pColumn->m_iIndex = 1;
	pColumn->m_strName = "Value";
	pColumns->Add(pColumn);

	return pColumns;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElement::GetListCtrlComparison()
//
//	Parameters:		pCompare - the element to be compared
//					iColumn  - the id of the column provided in the call to Sort
//
// 	Return Value:	-1 if less than, 0 if equal, 1 if greater than
//
// 	Description:	Called to compare this element to the specified element when 
//					sorting the list
//
//------------------------------------------------------------------------------
int CMLListCtrlElement::GetListCtrlComparison(CMLListCtrlElement* pElement, int iColumn)
{
	return -1;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElement::GetListCtrlText()
//
//	Parameters:		pColumns - the list of columns in the list box
//
// 	Return Value:	None
//
// 	Description:	Called to get the text for all columns in the list box
//
//------------------------------------------------------------------------------
void CMLListCtrlElement::GetListCtrlText(CMLListCtrlColumns* pColumns)
{
	CMLListCtrlColumn* pColumn = 0;

	for(int i = 0; i <= pColumns->GetUpperBound(); i++)
	{
		if((pColumn = pColumns->GetAt(i)) != 0)
			GetListCtrlText(pColumn);
	}

}

void CMLListCtrlElement::GetListCtrlText(CMLListCtrlColumn* pColumn)
{
	//	Which column?
	switch(pColumn->m_iIndex)
	{
		case 0:

			if(m_pData != NULL)
				pColumn->m_strText = m_pData->GetName(this->m_iOwner);
			break;

		case 1:

			if(m_pData != NULL)
				pColumn->m_strText = m_pData->GetValue(this->m_iOwner);
			break;

		default:

			pColumn->m_strText.Format("Unknown column index = %d", pColumn->m_iIndex);
			break;
	}

}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::Add()
//
//	Parameters:		pCtrlElement - pointer to object being added to the list
//
// 	Return Value:	TRUE if successful
//
// 	Description:	This function is called to add an object to the list
//
//------------------------------------------------------------------------------
BOOL CMLListCtrlElements::Add(CMLListCtrlElement* pCtrlElement)
{
	ASSERT(pCtrlElement);
	if(!pCtrlElement)
		return FALSE;

	try
	{
		AddTail(pCtrlElement);
		return TRUE;
	}
	catch(...)
	{
		return FALSE;
	}
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::CMLListCtrlElements()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Constructor
//
//------------------------------------------------------------------------------
CMLListCtrlElements::CMLListCtrlElements()
{
	m_NextPos = NULL;
	m_PrevPos = NULL;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::~CMLListCtrlElements()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Destructor
//
//------------------------------------------------------------------------------
CMLListCtrlElements::~CMLListCtrlElements()
{
	//	Flush the list and destroy the objects
	Flush(TRUE);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::Find()
//
//	Parameters:		pCtrlElement - pointer to object to search for
//
// 	Return Value:	NULL if not found.
//
// 	Description:	This function will find the position of the object in the
//					list.
//
//------------------------------------------------------------------------------
POSITION CMLListCtrlElements::Find(CMLListCtrlElement* pCtrlElement)
{
	return (CObList::Find(pCtrlElement));
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::First()
//
//	Parameters:		None
//
// 	Return Value:	A pointer to the first object in the list
//
// 	Description:	This function will retrieve the first object in the list.
//
//------------------------------------------------------------------------------
CMLListCtrlElement* CMLListCtrlElements::First()
{
	//	Get the first position
	m_NextPos = GetHeadPosition();
	m_PrevPos = NULL;

	if(m_NextPos == NULL)
		return NULL;
	else
		return (CMLListCtrlElement*)GetNext(m_NextPos);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::Flush()
//
//	Parameters:		bDelete - TRUE to delete all the objects
//
// 	Return Value:	None
//
// 	Description:	This function is called to empty the list
//
//------------------------------------------------------------------------------
void CMLListCtrlElements::Flush(BOOL bDelete)
{
	CMLListCtrlElement* pCtrlElement;

	//	Do we want to delete the objects?
	if(bDelete)
	{
		m_NextPos = GetHeadPosition();

		while(m_NextPos != NULL)
		{
			if((pCtrlElement = (CMLListCtrlElement*)GetNext(m_NextPos)) != 0)
				delete pCtrlElement;
		}

	}

	//	Remove all pointers from the list
	RemoveAll();
	m_NextPos = NULL;
	m_PrevPos = NULL;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::Last()
//
//	Parameters:		None
//
// 	Return Value:	A pointer to the last object in the list
//
// 	Description:	This function will retrieve the last object in the list.
//
//------------------------------------------------------------------------------
CMLListCtrlElement* CMLListCtrlElements::Last()
{
	//	Get the last position
	m_PrevPos = GetTailPosition();
	m_NextPos = NULL;

	if(m_PrevPos == NULL)
		return NULL;
	else
		return (CMLListCtrlElement*)GetPrev(m_PrevPos);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::Next()
//
//	Parameters:		None
//
// 	Return Value:	A pointer to the next object in the list
//
// 	Description:	This function will retrieve the next object in the list.
//
//------------------------------------------------------------------------------
CMLListCtrlElement* CMLListCtrlElements::Next()
{
	if(m_NextPos == NULL)
		return NULL;
	else
	{
		m_PrevPos = m_NextPos;
		GetPrev(m_PrevPos);
		return (CMLListCtrlElement*)GetNext(m_NextPos);
	}
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::Prev()
//
//	Parameters:		None
//
// 	Return Value:	A pointer to the previous object in the list
//
// 	Description:	This function will previous the first object in the list.
//
//------------------------------------------------------------------------------
CMLListCtrlElement* CMLListCtrlElements::Prev()
{
	if(m_PrevPos == NULL)
		return NULL;
	else
	{
		m_NextPos = m_PrevPos;
		GetNext(m_NextPos);
		return (CMLListCtrlElement*)GetPrev(m_PrevPos);
	}
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlElements::Remove()
//
//	Parameters:		pCtrlElement  - pointer to the object to be removed
//					bDelete - TRUE to delete the object after removal
//
// 	Return Value:	None
//
// 	Description:	This function will remove the object from the list
//
//------------------------------------------------------------------------------
void CMLListCtrlElements::Remove(CMLListCtrlElement* pCtrlElement, BOOL bDelete)
{
	POSITION Pos = Find(pCtrlElement);

	//	Is this object in the list
	if(Pos != NULL)
	{
		RemoveAt(Pos);

		//	Do we need to delete the object?
		if(bDelete)
			delete pCtrlElement;
	}
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumn::CMLListCtrlColumn()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Constructor
//
//------------------------------------------------------------------------------
CMLListCtrlColumn::CMLListCtrlColumn()
{
	m_strName = "";
	m_strText = "";
	m_iHeaderWidth = -1;
	m_iIndex = 0;
	m_iOwner = 0;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumn::~CMLListCtrlColumn()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Destructor
//
//------------------------------------------------------------------------------
CMLListCtrlColumn::~CMLListCtrlColumn()
{
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumns::Add()
//
//	Parameters:		pColumn - A pointer to the object being added
//
// 	Return Value:	None
//
// 	Description:	This function is called to add an object to the array
//
//------------------------------------------------------------------------------
void CMLListCtrlColumns::Add(CMLListCtrlColumn* pColumn)
{
	ASSERT(pColumn);
	
	//	Add the object to the array
	if(pColumn)
	{
		CObArray::Add(pColumn);
	}
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumns::CMLListCtrlColumns()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Constructor
//
//------------------------------------------------------------------------------
CMLListCtrlColumns::CMLListCtrlColumns() : CObArray()
{
	//	Set the initial size of the array
	SetSize(0);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumns::~CMLListCtrlColumns()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Destructor 
//
//------------------------------------------------------------------------------
CMLListCtrlColumns::~CMLListCtrlColumns()
{
	//	Flush the array and destroy the objects
	Flush(TRUE);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumns::Flush()
//
//	Parameters:		bDelete - TRUE to force deallocation of all objects
//
// 	Return Value:	None
//
// 	Description:	This function is called to remove all objects from the
//					array
//
//------------------------------------------------------------------------------
void CMLListCtrlColumns::Flush(BOOL bDelete)
{
	//	Do we want to delete the objects?
	if(bDelete)
	{
		for(int i = 0; i <= GetUpperBound(); i++)
		{
			if(GetAt(i) != 0)
				delete GetAt(i);
		}
	}

	//	Remove all pointers from the array
	RemoveAll();
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumns::GetAt()
//
//	Parameters:		iIndex - index of the object to retrieve
//
// 	Return Value:	A pointer to the specified object
//
// 	Description:	This function is called to retrieve the object at the 
//					specified index.
//
//------------------------------------------------------------------------------
CMLListCtrlColumn* CMLListCtrlColumns::GetAt(int iIndex)
{
	if((iIndex >= 0) && (iIndex <= GetUpperBound()))
		return ((CMLListCtrlColumn*)CObArray::GetAt(iIndex));
	else 
		return 0;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrlColumns::SetAt()
//
//	Parameters:		iIndex - index at which to place the object
//					pColumn - pointer to column being added to the array
//
// 	Return Value:	None
//
// 	Description:	This function is called to set the object at the 
//					specified index.
//
//------------------------------------------------------------------------------
void CMLListCtrlColumns::SetAt(int iIndex, CMLListCtrlColumn* pColumn)
{
	ASSERT(iIndex >= 0);
	ASSERT(pColumn != 0);

	CObArray::SetAtGrow(iIndex, pColumn);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Add()
//
//	Parameters:		pMLElement - pointer to element to be added to the list control
//
// 	Return Value:	TRUE if successful
//
// 	Description:	This function is called to add a new element to the list box
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::Add(CMLListCtrlElement* pMLElement, BOOL bResizeColumns)
{
	return Insert(pMLElement, -1, bResizeColumns);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Add()
//
//	Parameters:		pMLElements - a dynamic list of elements to be added
//					bClear - TRUE to clear existing contents
//
// 	Return Value:	None
//
// 	Description:	Called add all elements in the specified list
//
//------------------------------------------------------------------------------
void CMLListCtrl::Add(CObList* pMLElements, BOOL bClear)
{
	POSITION		Pos = NULL;
	CMLListCtrlElement*	pMLElement = NULL;

	if(bClear == TRUE)
		Clear();

	Pos = pMLElements->GetHeadPosition();
	while(Pos != NULL)
	{
		if((pMLElement = (CMLListCtrlElement*)(pMLElements->GetNext(Pos))) != 0)
			Add(pMLElement, FALSE);
	}

	AutoSizeColumns();
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Add()
//
//	Parameters:		pColumn - column to be added to the list view control
//					iIndex - index of column to be added
//
// 	Return Value:	TRUE if successful
//
// 	Description:	Called to add a column to the list view control
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::Add(CMLListCtrlColumn* pColumn, int iIndex)
{
	ASSERT(pColumn != 0);

	iIndex = this->InsertColumn(iIndex, pColumn->m_strName, LVCFMT_LEFT, 10);
	
	return (iIndex >= 0);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::AutoSizeColumn()
//
//	Parameters:		iIndex - index of the column to be sized
//					pColumn - the associated column descriptor
//
// 	Return Value:	None
//
// 	Description:	Called to resize the column to fit its content
//
//------------------------------------------------------------------------------
void CMLListCtrl::AutoSizeColumn(int iIndex, CMLListCtrlColumn* pColumn)
{
	int iData = 0;

	//	Get the width required to display the header
	if(pColumn->m_iHeaderWidth <= 0)
	{
		this->SetColumnWidth(iIndex, LVSCW_AUTOSIZE_USEHEADER);
		pColumn->m_iHeaderWidth = this->GetColumnWidth(iIndex);
	}

	//	Resize to fit the data
	this->SetColumnWidth(iIndex, LVSCW_AUTOSIZE);
	iData = this->GetColumnWidth(iIndex);
	if((iIndex == 0) && (m_bUseImages == TRUE))
	{
		iData += 16; // Allow for the image width
		this->SetColumnWidth(iIndex, iData);
	}

	//	Switch back to header width if it's greater
	if(pColumn->m_iHeaderWidth > iData)
		this->SetColumnWidth(iIndex, pColumn->m_iHeaderWidth);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::AutoSizeColumns()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Called to resize the columns to fit their content
//
//------------------------------------------------------------------------------
void CMLListCtrl::AutoSizeColumns()
{
	//	Size each of the columns
	if(m_pColumns != NULL)
	{
		//	Use the full client area if only one column in the list
		if(m_pColumns->GetUpperBound() == 0)
		{
			this->SetColumnWidth(0, -2);
		}
		else
		{
			for(int i = 0; i <= m_pColumns->GetUpperBound(); i++)
				AutoSizeColumn(i, m_pColumns->GetAt(i));
		}
	
	}// if(m_pColumns != NULL)

}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Clear()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Called to clear all elements in the list box
//
//------------------------------------------------------------------------------
void CMLListCtrl::Clear()
{
	m_bSuppress = TRUE;

	DeleteAllItems();

	m_bSuppress = FALSE;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::CMLListCtrl()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Constructor
//
//------------------------------------------------------------------------------
CMLListCtrl::CMLListCtrl()
{
	m_pColumns = 0;
	m_bSuppress = FALSE;
	m_bUseImages = FALSE;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::~CMLListCtrl()
//
//	Parameters:		None
//
// 	Return Value:	None
//
// 	Description:	Destructor
//
//------------------------------------------------------------------------------
CMLListCtrl::~CMLListCtrl()
{
	//	Delete the existing columns
	if(m_pColumns != 0)
	{
		m_pColumns->Flush(TRUE);
		delete m_pColumns;
		m_pColumns = 0;
	}

}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Delete()
// 
//	Parameters:		pMLElement     - the element to be selected
//					bSuppress - TRUE to suppress state change notifications
//
// 	Return Value:	None
//
// 	Description:	This function is called to delete an element in the list
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::Delete(CMLListCtrlElement* pMLElement, BOOL bSuppress)
{
	int iIndex;

	if((iIndex = GetIndex(pMLElement)) >= 0)
	{
		Delete(iIndex, bSuppress);
	}

	return (iIndex >= 0);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Delete()
// 
//	Parameters:		iIndex    - index of the element to delete
//					bSuppress - TRUE to suppress state change notifications
//
// 	Return Value:	None
//
// 	Description:	This function is called to delete an element in the list
//
//------------------------------------------------------------------------------
void CMLListCtrl::Delete(int iIndex, BOOL bSuppress)
{
	//	Is the index within range?
	if((iIndex >= 0) && (iIndex < GetItemCount()))
	{
		m_bSuppress = bSuppress;

		//	Delete the element
		DeleteItem(iIndex);

		m_bSuppress = FALSE;
	}	
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::DoComparison()
//
//	Parameters:		LPItem1  - First element for comparison
//					LPItem2  - Second element for comparison
//					LPColumn - Index of column to sort on
//
// 	Return Value:	> 0 if LPItem1 is greater than LPItem2
//					= 0 if LPItem1 equals LPItem2
//					< 0 if LPItem1 is less than LPItem2
//
// 	Description:	This function is called by Windows to sort the list box
//
//------------------------------------------------------------------------------
int CALLBACK CMLListCtrl::DoComparison(LPARAM LPItem1,LPARAM LPItem2,LPARAM LPColumn)
{
	CMLListCtrlElement* pMLElement1 = (CMLListCtrlElement*)LPItem1;
	CMLListCtrlElement* pMLElement2 = (CMLListCtrlElement*)LPItem2;

	return pMLElement1->GetListCtrlComparison(pMLElement2, (int)LPColumn);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::GetIndex()
//
//	Parameters:		pMLElement - pointer to object to search for
//
// 	Return Value:	The index of the object in the list
//
// 	Description:	This function is called to get the list index of the
//					specified object.
//
//------------------------------------------------------------------------------
int CMLListCtrl::GetIndex(CMLListCtrlElement* pMLElement)
{
	LVFINDINFO	Find;

	ZeroMemory(&Find, sizeof(Find));
	Find.flags = LVFI_PARAM;
	Find.lParam = (LPARAM)pMLElement;

	return FindItem(&Find);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::GetSelection()
//
//	Parameters:		None
//
// 	Return Value:	The element currently selected in the list box
//
// 	Description:	Called to get the element that is the current selection
//
//------------------------------------------------------------------------------
CMLListCtrlElement* CMLListCtrl::GetSelection()
{
	POSITION		Pos = NULL;
	CMLListCtrlElement*	pMLElement = 0;
	int				iIndex = -1;

	if((Pos = this->GetFirstSelectedItemPosition()) != NULL)
	{
		if((iIndex = this->GetNextSelectedItem(Pos)) >= 0)
			pMLElement = (CMLListCtrlElement*)(this->GetItemData(iIndex));
	}

	return pMLElement;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Initialize()
//
//	Parameters:		pOwnerClass - an object of the class to be displayed
//					uResourceId - resource id for image list images
//
// 	Return Value:	None
//
// 	Description:	TRUE if successful
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::Initialize(CMLListCtrlElement* pOwnerClass, UINT uResourceId)
{
	CBitmap bmImages;

	SetExtendedStyle(GetExtendedStyle() | LVS_EX_FULLROWSELECT);

	//	Should we create an image list?
	if(uResourceId > 0)
	{
		//	Create the image list
		bmImages.LoadBitmap(uResourceId);
		m_aImages.Create(16, 16, ILC_MASK | ILC_COLOR24, 0, 1);
		m_aImages.Add(&bmImages, RGB(255,0,255));  

		//	Attach the image list
		SetImageList(&m_aImages, LVSIL_SMALL);

		m_bUseImages = TRUE;
	}

	//	Delete any columns that may have already been added to the list view
	if(this->GetHeaderCtrl() != 0)
	{
		while(this->GetHeaderCtrl()->GetItemCount() > 0)	
			this->DeleteColumn(0);
	}
	
	//	Delete the existing columns
	if(m_pColumns != 0)
	{
		m_pColumns->Flush(TRUE);
		delete m_pColumns;
		m_pColumns = 0;
	}

	//	Get the new column collection from the owner
	if(pOwnerClass != 0)
		m_pColumns = pOwnerClass->GetListCtrlColumns();

	//	Add the new columns
	if(m_pColumns != 0)
	{
		for(int i = 0; i <= m_pColumns->GetUpperBound(); i++)
		{
			if(m_pColumns->GetAt(i) != 0)
				Add(m_pColumns->GetAt(i), i);
		}

		AutoSizeColumns();

	}

	return TRUE;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Insert()
//
//	Parameters:		pMLElement - pointer to element to be added to the list control
//					iIndex - index at which to insert the element
//
// 	Return Value:	TRUE if successful
//
// 	Description:	This function is called to insert an element at the specified
//					index
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::Insert(CMLListCtrlElement* pMLElement, int iIndex, BOOL bResizeColumns)
{
	LV_ITEM	lvItem;
	BOOL	bSuccessful = FALSE;

	ASSERT(pMLElement);

	//	Is the index within range
	if((iIndex < 0) || (iIndex >= this->GetItemCount()))
		iIndex = this->GetItemCount();

	memset(&lvItem, 0, sizeof(lvItem));
	lvItem.mask = LVIF_TEXT | LVIF_PARAM;
	lvItem.iItem = iIndex;
	lvItem.pszText = LPSTR_TEXTCALLBACK;
	lvItem.lParam = (LPARAM)pMLElement;

	//	Are we using images?
	if(m_bUseImages == TRUE)
	{
		lvItem.mask |= LVIF_IMAGE;
		lvItem.iImage = I_IMAGECALLBACK;
	}

	if((bSuccessful = (CListCtrl::InsertItem(&lvItem) != -1)) == TRUE)
	{
		if(bResizeColumns ==  TRUE)
			AutoSizeColumns();
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::OnGetDispInfo()
//
//	Parameters:		pNMHDR  - pointer to message header structure
//					pResult - pointer to return value buffer
//
// 	Return Value:	None
//
// 	Description:	This function is called by the control to get the 
//					information it needs to display a list box selection
//
//------------------------------------------------------------------------------
void CMLListCtrl::OnGetDispInfo(NMHDR* pNMHDR, LRESULT* pResult) 
{
	LV_DISPINFO*	pDispInfo = (LV_DISPINFO*)pNMHDR;
	CMLListCtrlElement*	pMLElement = NULL;
	CMLListCtrlColumn*	pColumn = NULL;

	if(pResult) *pResult = 0;
	
	//	Get the list element
	if((pMLElement = (CMLListCtrlElement*)(pDispInfo->item.lParam)) != NULL)
	{
		//	Get the column
		if((m_pColumns != NULL) && (pDispInfo->item.iSubItem >= 0) && (pDispInfo->item.iSubItem <= m_pColumns->GetUpperBound()))
			pColumn = m_pColumns->GetAt(pDispInfo->item.iSubItem);
		
	}// if((pMLElement = (CMLListCtrlElement*)(pDispInfo->item.lParam)) != NULL)	

	//	Do we need the display text for this column?
	if((pColumn != NULL) && (pDispInfo->item.mask & LVIF_TEXT))
	{
		//	Get the column text
		pMLElement->GetListCtrlText(pColumn);

		//	Update the control
		lstrcpy(pDispInfo->item.pszText, pColumn->m_strText);

	}

	//	Do we need the image index for this row?
	if((pMLElement != NULL) && (pDispInfo->item.mask & LVIF_IMAGE))
	{
		pDispInfo->item.iImage = pMLElement->GetListCtrlImage();
	}

}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::OnItemChanged()
//
//	Parameters:		pNMHDR  - pointer to message header structure
//					pResult - pointer to return value buffer
//
// 	Return Value:	TRUE to discard the notification
//
// 	Description:	This function is called when the selection state of an element
//					has changed.
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::OnItemChanged(NMHDR* pNMHDR, LRESULT* pResult) 
{
	if(m_bSuppress)
	{
		*pResult = 0;
		return TRUE;
	}
	else
	{
		*pResult = 0;
		return FALSE;
	}
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::OnItemChanging()
//
//	Parameters:		pNMHDR  - pointer to message header structure
//					pResult - pointer to return value buffer
//
// 	Return Value:	TRUE to discard the notification
//
// 	Description:	This function is called when the selection state of an element
//					is about to change.
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::OnItemChanging(NMHDR* pNMHDR, LRESULT* pResult) 
{
	if(m_bSuppress)
	{
		*pResult = 0;
		return TRUE;
	}
	else
	{
		*pResult = 0;
		return FALSE;
	}
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Redraw()
//
//	Parameters:		iIndex - index of the element to redraw
//
// 	Return Value:	None
//
// 	Description:	This function is called to redraw an element in the list
//
//------------------------------------------------------------------------------
void CMLListCtrl::Redraw(int iIndex)
{
	//	Is the index within range?
	if((iIndex >= 0) && (iIndex < this->GetItemCount()))
	{
		RedrawItems(iIndex, iIndex);
	}	
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Redraw()
//
//	Parameters:		pMLElement - the element to be refreshed
//
// 	Return Value:	None
//
// 	Description:	This function is called to redraw an element in the list
//
//------------------------------------------------------------------------------
void CMLListCtrl::Redraw(CMLListCtrlElement* pMLElement)
{
	int iIndex = -1;

	if((iIndex = GetIndex(pMLElement)) >= 0)
		Redraw(iIndex);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Select()
// 
//	Parameters:		iIndex    - index of the element to select
//					bSuppress - TRUE to suppress state change notifications
//
// 	Return Value:	None
//
// 	Description:	This function is called to select an element in the list
//
//------------------------------------------------------------------------------
void CMLListCtrl::Select(int iIndex, BOOL bSuppress)
{
	//	Is the index within range?
	if((iIndex >= 0) && (iIndex < GetItemCount()))
	{
		m_bSuppress = bSuppress;

		//	Select the element
		SetItemState(iIndex, LVIS_SELECTED, LVIS_SELECTED);
		SetItemState(iIndex, LVIS_FOCUSED, LVIS_FOCUSED);
		EnsureVisible(iIndex, FALSE);

		m_bSuppress = FALSE;
	}	
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Select()
// 
//	Parameters:		pMLElement     - the element to be selected
//					bSuppress - TRUE to suppress state change notifications
//
// 	Return Value:	None
//
// 	Description:	This function is called to select an element in the list
//
//------------------------------------------------------------------------------
BOOL CMLListCtrl::Select(CMLListCtrlElement* pMLElement, BOOL bSuppress)
{
	int iIndex;

	if((iIndex = GetIndex(pMLElement)) >= 0)
	{
		Select(iIndex, bSuppress);
	}

	return (iIndex >= 0);
}

//------------------------------------------------------------------------------
//
// 	Function Name:	CMLListCtrl::Sort()
//
//	Parameters:		iColumn - the id of the column to be sorted
//
// 	Return Value:	None
//
// 	Description:	This function is called to sort the list box.
//
//------------------------------------------------------------------------------
void CMLListCtrl::Sort(int iColumn) 
{
	SortItems(DoComparison, iColumn);
}

