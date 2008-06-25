/**
 * Copyright (C) 2005-2008 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * and Open Source Software ("FLOSS") applications as described in Alfresco's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * http://www.alfresco.com/legal/licensing
 */

/**
 * CustomiseDashlets component.
 *
 * @namespace Alfresco
 * @class Alfresco.CustomiseDashlets
 */
(function()
{

   var Dom = YAHOO.util.Dom;
   var Event = YAHOO.util.Event;
   var DDM = YAHOO.util.DragDropMgr;

   /**
    * Alfresco.CustomiseDashlets constructor.
    *
    * @param {string} htmlId The HTML id of the parent element
    * @return {Alfresco.CustomiseDashlets} The new CustomiseDashlets instance
    * @constructor
    */
   Alfresco.CustomiseDashlets = function(htmlId)
   {
      this.name = "Alfresco.CustomiseDashlets";
      this.id = htmlId;      

      // Register this component
      Alfresco.util.ComponentManager.register(this);

      // Load YUI Components
      Alfresco.util.YUILoaderHelper.require(["button", "container", "datatable", "datasource", "dragdrop"], this.onComponentsLoaded, this);

      return this;
   }

   Alfresco.CustomiseDashlets.prototype =
   {

      /**
       * Object container for storing YUI widget instances.
       * 
       * @property widgets
       * @type object
       */
      widgets: {},

      /**
       * Cache for YAHOO.util.KeyListener objects for each dashlet-li.
       *
       * @property keyListeners
       * @type object
       */
      keyListeners:{},

      /**
       * The drag'n'drop group for where dashlets can be dropped to be added.
       *
       * @property DND_GROUP_ADD_DASHLET
       * @type String
       */
      DND_GROUP_ADD_DASHLET: "CUSTOMISE_DASHLET.DND_GROUP.ADD_DASHLET",

      /**
       * The drag'n'drop group for where dashlets can be dropped to be deleted.
       *
       * @property DND_GROUP_DELETE_DASHLET
       * @type String
       */
      DND_GROUP_DELETE_DASHLET: "CUSTOMISE_DASHLET.DND_GROUP.DELETE_DASHLET",

      /**
       * To let various methods no what dashlet that is currently selected, if any.
       *
       * @property currentDashletEl
       * @type HTMLElement of type li
       */
      currentDashletEl: null,

      /**
       * A reference to the "invisible" object that is used to "make space"
       * for the new dashlet during drag n drop to the columns.
       *
       * @property shadow
       * @type HTMLElement of type li
       */
      shadow: null,
      
      /**
       * Set messages for this module.
       *       
       * @method setMessages
       * @param obj {object} Object literal specifying a set of messages
       * @return {Alfresco.CustomiseDashlets} returns 'this' for method chaining
       */
      setMessages: function CD_setMessages(obj)
      {
         Alfresco.util.addMessages(obj, this.name);
         return this;
      },

      /**
       * Fired by YUILoaderHelper when required component script files have
       * been loaded into the browser.
       *
       * @method onComponentsLoaded
       */
      onComponentsLoaded: function FU_onComponentsLoaded()
      {
         // Shortcut for dummy instance
         if (this.id === null)
         {
            return;
         }

         // Save reference to buttons so we can change label and such later
         this.widgets.toggleDashletsButton = Alfresco.util.createYUIButton(this, "toggleDashlets-button", this.onToggleDashletsButtonClick);
         this.widgets.doneButton = Alfresco.util.createYUIButton(this, "done-button", this.onDoneButtonClick);
         this.widgets.cancelButton = Alfresco.util.createYUIButton(this, "cancel-button", this.onCancelButtonClick);

         // Save a reference to the shadow that will be used during drag n drop
         this.shadow = Dom.get(this.id + "-dashlet-li-shadow")

         // Make only column 1-n lists drop targets for add
         // In other words ignore the dashlet column (column 0)
         for (var i = 1; true ; i++)
         {
            var ul = Dom.get(this.id + "-column-ul-" + i);
            if(ul)
            {
               new YAHOO.util.DDTarget(ul, this.DND_GROUP_ADD_DASHLET);
            }
            else
            {
               break;
            }
         }

         // Save a reference to the dashlet list and garbage can
         this.widgets.dashletListEl = Dom.get(this.id + "-column-ul-0");
         this.widgets.trashcanListEl = Dom.get(this.id + "-trashcan-img");

         // ... and create a delete drop target on them
         new YAHOO.util.DDTarget(this.widgets.dashletListEl, this.DND_GROUP_DELETE_DASHLET);
         new YAHOO.util.DDTarget(this.widgets.trashcanListEl, this.DND_GROUP_DELETE_DASHLET);

         // Make all dashlets in all columns draggable
         var noOfColumns = i - 1;
         for (i = 0; i <= noOfColumns; i++) {
            for (var j = 1; true; j++)
            {
               var li = Dom.get(this.id + "-dashlet-li-" + i + "-" + j)
               if(li)
               {
                  this._createDashlet(li);
               }
               else
               {
                  break;
               }
            }
         }

      },

      /**
       * Fired when the user clicks the Hide / Show dashlet button.
       * Hides or shows the dashlet list.
       *
       * @method onToggleDashletsButtonClick
       * @param event {object} an "click" event
       */
      onToggleDashletsButtonClick: function CD_onToggleDashletsButtonClick(event)
      {
         var dashletListDiv = Dom.get(this.id + "-dashlets-div");
         if(Dom.hasClass(dashletListDiv, "hiddenComponents"))
         {
            Dom.removeClass(dashletListDiv, "hiddenComponents")
            this.widgets.toggleDashletsButton.set("label", Alfresco.util.message("button.hideDashlets", this.name));
         }
         else
         {
            Dom.addClass(dashletListDiv, "hiddenComponents")            
            this.widgets.toggleDashletsButton.set("label", Alfresco.util.message("button.showDashlets", this.name));            
         }
      },

      /**
       * Fired when the user clicks the Cancel button.
       * Takes the user back to the dashboard page without saving anything.
       *
       * @method onCancelButtonClick
       * @param event {object} a "click" event
       */
      onCancelButtonClick: function CD_onCancelButtonClick(event)
      {
         document.location.href = Alfresco.constants.URL_CONTEXT + "page/user-dashboard";
      },

      /**
       * Fired when the user tabs from a "dashlet" (or selects another dashlet
       * or something else).
       * Since browsers only gives focus on links and form elements its
       * actually a hidden link that loses the focus and makes this method get
       * called.
       *
       * Removes the "focused" class from the dashlet-li so it appears to be
       * de-focused or de-selected.
       *
       * Removes the keylistener for this dashlet.
       *
       * @method onDashletBlur
       * @param event {object} a "blur" event
       */
      onDashletBlur: function CD_onDashletBlur(event, li)
      {
         // Remove the "focused" class from the dashlet so it doesn't appera to be selected
         if(this.currentDashletEl)
         {
            Dom.removeClass(this.currentDashletEl, "focused");
         }
         Dom.removeClass(li, "focused");
         this.currentDashletEl = null;

         // Stop listening to key events
         var kl = this.keyListeners[li.id];
         if(kl !== undefined)
         {
            kl.disable();
         }
      },

      /**
       * Fired when the user tabs to a "dashlet" (or clicks the dashlet).
       * Since browsers only gives focus on links and form elements its
       * actually a hidden link that gets the focus and makes this method get
       * called.
       *
       * Adds the "focused" class to the dashlet-li so it appears to be
       * focused or selected.
       *
       * Adds a keylistener for this dashlet so we can listen for keystrokes.
       *
       * @method onDashletFocus
       * @param event {object} a "focus" event
       */
      onDashletFocus: function(event, li)
      {
         // Remove the focused class from no longer selected dashlets and add it to the current
         if(this.currentDashletEl)
         {
            Dom.removeClass(this.currentDashletEl, "focused");
         }
         this.currentDashletEl = li;
         Dom.addClass(this.currentDashletEl, "focused");

         /**
          * Add key listeners to the a href tag that actually is the element
          * with the focus, receive events in the onKeyPressed() method.
          * Note that we cannot attach the KeyListener to a global element
          * such as the document since several components that listens to key
          * events might live on the same page.
          */
         var a = Dom.getFirstChild(li);
         var kl = this.keyListeners[li.id];
         if(kl === undefined)
         {
            kl = new YAHOO.util.KeyListener(
                  a,
            {
               keys: [
                  YAHOO.util.KeyListener.KEY.UP,
                  YAHOO.util.KeyListener.KEY.DOWN,
                  YAHOO.util.KeyListener.KEY.LEFT,
                  YAHOO.util.KeyListener.KEY.RIGHT,
                  YAHOO.util.KeyListener.KEY.ESCAPE,
                  YAHOO.util.KeyListener.KEY.DELETE,
                  YAHOO.util.KeyListener.KEY.ENTER,
                  YAHOO.util.KeyListener.KEY.SPACE
               ]
            },
            {
               fn: this.onKeyPressed,
               scope: this,
               correctScope: true
            });
            this.keyListeners[li.id] = kl;
         }
         kl.enable();
      },

      /**
       * Listens to key events for the currently selected dashlet
       * (or in reality currently focused a href element).
       *
       * Will do the following for dashlets in a column:
       * - move the dashlet between columns when LEFT or RIGHT is clicked.
       * - mode the dashlet in the current column when UP or DOWN is clicked.
       * - delete the dashlet when DELETE is clicked.
       * - deselect a dashlet when ESCAPE is clicked.
       *
       * Will do the following for dashlets available to columns:
       * - add a dashlet to the first column with free space if ENTER is clicked.
       *
       * @method onKeyPressed
       * @param event {object} a "key" event
       */
      onKeyPressed: function FU_onKeyPressed(event, id)
      {
         var currentDashlet = this.currentDashletEl;
         var relativeNode;
         var destColumn

         if (id[1].keyCode === YAHOO.util.KeyListener.KEY.ESCAPE)
         {
            // Deselect the current dashlet when escape is clicked
            this.focusDashletAfterDomChange(currentDashlet, false);
         }
         else if (id[1].keyCode === YAHOO.util.KeyListener.KEY.ENTER)
         {
            // Was enter hit for a dashlet available to columns?
            if (!this.isColumnDashlet(currentDashlet))
            {
               // Yes it was, find the first column with space for a new column.
               for (var i = 1; true; i++)
               {
                  destColumn = Dom.get(this.id + "-column-ul-" + i);
                  if(destColumn)
                  {
                     if(!this.isColumnFull(destColumn))
                     {
                        /**
                         * We have found an column with free space, make a copy
                         * of the dashlet and insert it in the first position.
                         */
                        var children = Dom.getChildrenBy(destColumn, this.isRealDashlet);
                        this.copyAndInsertDashlet(currentDashlet, destColumn, children.length > 0 ? children[0] : null)
                        return;
                     }
                  }
                  else
                  {
                     break;
                  }
               }
               // No columns with free space was found, alert the user
               Alfresco.util.PopupManager.displayMessage(
               {
                  text: Alfresco.util.message("message.allColumnsAreFull", this.name)
               });
            }

         }
         else if (id[1].keyCode === YAHOO.util.KeyListener.KEY.DELETE)
         {
            // Make sure we don't delete any dashlet from the available list
            if( this.isColumnDashlet(currentDashlet))
            {
               this.deleteDashlet(currentDashlet);
            }
         }
         else if (this.isColumnDashlet(currentDashlet))
         {
            // UP, DOWN, LEFT & RIGHT key events apply only to column dashlets.
            if (id[1].keyCode === YAHOO.util.KeyListener.KEY.UP)
            {
               relativeNode = Dom.getPreviousSiblingBy(currentDashlet, this.isRealDashlet);
               if (relativeNode)
               {
                  // Found a dashlet above, move the current one above it
                  Dom.insertBefore(currentDashlet, relativeNode);
                  this.focusDashletAfterDomChange(currentDashlet, true);
               }
            }
            else if (id[1].keyCode === YAHOO.util.KeyListener.KEY.DOWN)
            {
               relativeNode = Dom.getNextSiblingBy(currentDashlet, this.isRealDashlet);
               if(relativeNode)
               {
                  // Found a dashlet below, move the current one beneath it
                  Dom.insertAfter(currentDashlet, relativeNode);
                  this.focusDashletAfterDomChange(currentDashlet, true);
               }
            }
            else
            {
               // Find a column index for the column to the left or right of the current dashlet
               var column = this.getColumnIndex(currentDashlet);
               if (id[1].keyCode === YAHOO.util.KeyListener.KEY.LEFT)
               {
                  column = column -1;
               }
               else if (id[1].keyCode === YAHOO.util.KeyListener.KEY.RIGHT)
               {
                  column = column +1;
               }
               else
               {
                  column = -1;
               }
               // Look for the column
               destColumn = Dom.get(this.id + "-column-ul-" + column)
               if (column > 0 && destColumn)
               {
                  // A column was found, make sure it has free space
                  if (!this.isColumnFull(destColumn))
                  {
                     // Insert the dashlet in the same position as it had in the previous column
                     var index = this._getDashletIndex(currentDashlet);
                     relativeNode = this._getDashlet(destColumn, index);
                     if (relativeNode)
                     {
                        Dom.insertBefore(currentDashlet, relativeNode);
                     }
                     else
                     {
                        destColumn.appendChild(currentDashlet);
                     }
                     /**
                      * When the Dom changes we need to "remind" the browser what
                      * element that has the current focus.
                      */
                     this.focusDashletAfterDomChange(currentDashlet, true);
                  }
               }
            }
         }
      },

      /**
       * Creates a draggable Alfresco.CustomiseDashlets.Dashlet
       * that listens to blur, focus and click elements.
       *
       * @method _createDashlet
       * @param li {HTMLElement} of type li
       * @private
       */
      _createDashlet: function CD__createDashlet(li)
      {
         // Setup drag n drop support
         var d = new Alfresco.CustomiseDashlets.DashletProxy(li, this.shadow, this);
         d.addToGroup(this.DND_GROUP_ADD_DASHLET);
         d.addToGroup(this.DND_GROUP_DELETE_DASHLET);

         // Find hidden link to add tab support
         var a = Dom.getFirstChild(li);
         var aEl = new YAHOO.util.Element(a);
         aEl.addListener("focus", this.onDashletFocus, li, this);
         aEl.addListener("blur", this.onDashletBlur, li, this);

         // Add select support when using mouse
         var liEl = new YAHOO.util.Element(li);
         liEl.addListener("click", function(e, obj)
         {
            obj.focus(); // will call selectDashlet
         }, a, this);
      },

      /**
        * Creates a copy of the dragged dashlet 'srcEl' and inserts in column
        * 'destUl' before 'insertBeforeNode'.
        *
        * @method copyAndInsertDashlet
        * @param srcEl {HTMLElement} of type li
        * @param destUl {HTMLElement} of type ul
        * @param insertBeforeNode {HTMLElement} of type li
        */
      copyAndInsertDashlet: function CD_copyAndInsertDashlet(srcEl, destUl, insertBeforeNode)
      {
         /**
          * Don't do a cloneNode copy since it will make IE point the
          * new Dashlets a.focus handler to elSrc.
          * Create a new one and use innerHTML instead.
          */
         var copy = document.createElement("li");
         copy.id = srcEl.id + Dom.generateId();
         Dom.addClass(copy, "dashlet-item");
         copy.innerHTML = srcEl.innerHTML + "";

         // Make the dashlet draggable and selectable/focusable.
         this._createDashlet(copy);

         // Make sure the new dashlet is visible to the user.
         Dom.setStyle(copy, "visibility", "");
         Dom.setStyle(copy, "display", "");
         if(insertBeforeNode)
         {
            // Insert it after the specified node
            destUl.insertBefore(copy, insertBeforeNode);
         }
         else
         {
            // Or last if no node was specified.
            destUl.appendChild(copy);
         }
         // Make sure the new dashlet gets the focus.
         this.focusDashletAfterDomChange(copy, true);
      },

      /**
       * Gives or takes the focus from 'li' depending on 'focus'.
       * Should be called when a Dom change has occured to "restore" the focus.
       *
       * @method focusDashletAfterDomChange
       * @param li {HTMLElement} of type li
       * @param focus {boolean} true if li should get focus, false if it should loose focus
       */
      focusDashletAfterDomChange: function CD_focusDashletAfterDomChange(li, focus)
      {
         var doFocus = focus;
         var a = Dom.getFirstChild(li);

         // Don't call it directly, give the browser 50 ms to fix the Dom first.
         YAHOO.lang.later(50, a, function()
         {
            if (doFocus)
            {
               a.focus();
            }
            else
            {
               a.blur();
            }
         });
      },

      /**
       * Deletes the dashlet from the Dom.
       *
       * @method deleteDashlet
       * @param li {HTMLElement} of type li (the dashlet to be deleted)
       */
      deleteDashlet: function(li)
      {
         // Remove the dashlet from the Dom.
         li.parentNode.removeChild(li);

         // Hide the shadow object
         Dom.setStyle(this.shadow, "display", "none");
      },

      /**
       * Helper function to get a dashlet from a specific index in a column.
       *
       * @method _getDashlet
       * @param ul {HTMLElement} of type ul (the dashlet's column)
       * @param index {int} index position of the dashlet
       * @return {HTMLElement} of type li (the dashlet)
       */
      _getDashlet: function CD__getDashlet(ul, index)
      {
         return Dom.getElementsByClassName("dashlet-item", "li", ul)[index];
      },

      /**
       * Helper function to get a dashlet's index in it's column.
       *
       * @method _getDashletIndex
       * @param li {HTMLElement} of type li (the dashlet)
       * @return {int} index position of the dashlet
       */
      _getDashletIndex: function CD__getDashletIndex(li)
      {
         var ul = li.parentNode;
         var dashlets = Dom.getElementsByClassName("dashlet-item", "li", ul);
         for (var i = 0; i < dashlets.length; i++)
         {
            if (dashlets[i] === li)
            {
               return i;
            }
         }
         return -1;
      },

      /**
       * Helper function to get the index of the dashlet's column.
       *
       * @method getColumnIndex
       * @param li {HTMLElement} of type li (the dashlet)
       * @return {int} the column index
       */
      getColumnIndex: function CD_getColumnIndex(li)
      {
         if (li.nodeName.toLowerCase() === "li")
         {
            var ul = li.parentNode;
            if (ul.nodeName.toLocaleLowerCase() === "ul")
            {
               var column = ul.id.substr(ul.id.lastIndexOf("-") + 1);
               return parseInt(column);
            }
         }
         return -1;
      },

      /**
       * Helper function to determine the number of dashlets inside the column.
       *
       * @method getNoOfDashlets
       * @param ul {HTMLElement} of type ul (the column)
       * @return {int} the number of dashlets inside column ul.
       */
      getNoOfDashlets: function CD_getNoOfDashlets(ul)
      {
         var dashlets = Dom.getElementsByClassName("dashlet-item", "li", ul);
         return dashlets ? dashlets.length : 0;
      },

      /**
       * Helper function to determine if a column can't fit anymore dashlets.
       *
       * @method isColumnFull
       * @param ul {HTMLElement} of type ul (the dashlet column)
       * @return {boolean} true if column is full
       */
      isColumnFull: function CD_isColumnFull(ul)
      {
         return this.getNoOfDashlets(ul) >= 5;
      },

      /**
       * Helper function to determine if dashlet is "column dashlet",
       * in other words not a "available dashlet".
       *
       * @method isColumnDashlet
       * @param li {HTMLElement} of type li (the dashlet)
       * @return {int} the column index
       */
      isColumnDashlet: function DC_isColumnDashlet(li)
      {
         return this.getColumnIndex(li) > 0;
      },

      /**
       * Helper function to determine if el is a dashlet.
       * Checked performed by looking of it has class "dashlet-item".
       *
       * @method isRealDashlet
       * @param el {HTMLElement} element to test
       * @return {boolean} true if el has class "adshlet-item"
       */
      isRealDashlet: function CD_isRealDashlet(el)
      {
         return Dom.hasClass(el, "dashlet-item");
      },

      /**
       * Helper function to determine if an element (el) is a certain type (tagType).
       *
       * @method isOfTagType
       * @param el {HTMLElement} element to test tag type of
       * @param tagType {string} tag type
       * @return {boolean} true if el's tag type is same as tagType
       */
      isOfTagType: function CD_isOfTagType(el, tagType)
      {
         return el.nodeName.toLowerCase() === tagType;
      },

      /**
       * Helper function to determine if an element is an add drop target.
       *
       * @method isAddTarget
       * @param el {HTMLElement} of type li or ul to test
       * @return {boolean} true if el should be considered as a add drop target
       */
      isAddTarget: function CD_isAddTarget(el)
      {
         // Either el is a column/ul ...
         if (el === this.widgets.dashletListEl ||
            el === this.widgets.trashcanListEl)
         {
            return false;
         }
         else if (el){
            // .. or it was a dashlet/li, then check its column/ul instead. 
            el = el.parentNode;
            if(el === this.widgets.dashletListEl ||
               el === this.widgets.trashcanListEl)
            {
               return false;
            }
         }
         return true;
      }

   };


   /**
    * Alfresco.CustomiseDashlets.DashletProxy constructor.
    *
    * Alfresco.CustomiseDashlets.DashletProxy is a class that represents a dragged dashlet.
    * It extends the yui class YAHOO.util.DDProxy that gives access to most of
    * the needed properties during a drag n drop operation.
    *
    * @param {HTMLElement} of type li
    * @param {HTMLElement} of type li, a shared "invisible" dashlet that creates "space" in the list during drag n drop.
    * @param {Alfresco.CustomiseDashlets} the component (for helper functions and the current context such as selected Dashlet etc)
    * @param {string} the component (for helper functions and the current context such as selected Dashlet etc)
    * @return {Alfresco.CustomiseDashlets} The new CustomiseDashlets instance
    * @constructor
    */
   Alfresco.CustomiseDashlets.DashletProxy = function(li, shadow, customiseDashletComponent) {

      Alfresco.CustomiseDashlets.DashletProxy.superclass.constructor.call(this, li);

      // Make the drag proxy slightly transparent
      var el = this.getDragEl();
      Dom.setStyle(el, "opacity", 0.67); // The proxy is slightly transparent

      // Keep track of mouse drag movements
      this.goingUp = false;
      this.lastY = 0;

      // Save a local copy of the shared shadow element.
      this.srcShadow = shadow;

      // Property to remember the element that the proxy was dropped on.
      this.droppedOnEl= null;

      // Save a reference to the component.
      this.customiseDashletComponent = customiseDashletComponent;

   };

   YAHOO.extend(Alfresco.CustomiseDashlets.DashletProxy, YAHOO.util.DDProxy, {

      /**
       * Callback for when the user drags the dashlet.
       * Will style the proxy to match the dashlet.
       *
       * @method startDrag
       * @param x {int} the x position of where the drag started
       * @param y {int} the y position of where the drag started
       */
      startDrag: function CD_startDrag(x, y)
      {
         // A new drag operation has started, make sure the droppedOnEl is reset.
         this.droppedOnEl = null;

         // Remove the selection of the previously focused dashlet.
         if (this.customiseDashletComponent.currentDashletEl)
         {
            this.customiseDashletComponent.currentDashletEl.blur();
            this.customiseDashletComponent.currentDashletEl = null;
         }

         // Make the proxy look like the source element.
         var dragEl = this.getDragEl();
         var srcEl = this.getEl();
         dragEl.innerHTML = srcEl.innerHTML;
         Dom.addClass(dragEl, "dashlet-item");
         Dom.addClass(dragEl, "focused");

         // Since the proxy looks like the dashlet we can hide the actual dashlet
         if (this.customiseDashletComponent.getColumnIndex(srcEl) > 0)
         {
            Dom.setStyle(srcEl, "visibility", "hidden");
         }

         // Prepare shadow for drag n drop session
         this._resetSrcShadow();
      },

      /**
       * Reset the shadow so its ready to be used when the proxy is
       * dragged over other dashlets.
       *
       * @method _resetSrcShadow
       */
      _resetSrcShadow: function CD__resetSrcShadow()
      {
         var srcEl = this.getEl();
         var p = srcEl.parentNode;
         if (this.customiseDashletComponent.getColumnIndex(srcEl) === 0)
         {
            Dom.setStyle(this.srcShadow, "display", "none");
         }
         Dom.setStyle(this.srcShadow, "visibility", "hidden");

         p.insertBefore(this.srcShadow, srcEl);
      },

      /**
       * Callback for when the drag n drop session is over, is called even if
       * the proxy wasn't dropped on a target.
       *
       * Will either delete, add or leave the dashlet depending on where the
       * dashlet was dropped.
       *
       * @method endDrag
       * @param e {int}
       * @param id {string}
       */
      endDrag: function CD_endDrag(e, id)
      {
         // Get the actual dashlet and the proxy
         var srcEl = this.getEl();
         var proxy = this.getDragEl();

         // Check if the dashlet should be deleted
         if (!this.customiseDashletComponent.isAddTarget(this.droppedOnEl))
         {
            // The proxy was dropped on a delete target
            if (this.customiseDashletComponent.isColumnDashlet(srcEl))
            {
               // Only delete the dashlet if its a "column dashlet"
               this.customiseDashletComponent.deleteDashlet(srcEl);
            }
            // Return so we don't add the dashlet.
            return;
         }

         /**
          * If we get here, the dashlet was either dropped on a add target,
          * the original column or just "dropped" outside a any target.
          * Either way animate the proxy to "fly" towards the shadow.
          * Since we have used the shadow to make space for the dashlet during
          * the drag we can rely on that the shadow is in the position we're
          * the dashlet should be placed.
          *
          * We will decide later if its an add or move that has been performed.
          */

         // Show the proxy element and animate it towards the shadow.
         Dom.setStyle(proxy, "visibility", "");
         var a = new YAHOO.util.Motion(proxy, {points: {to: Dom.getXY(this.srcShadow)}},
               0.3,
               YAHOO.util.Easing.easeOut
               );

         // Save the scope of this for the callback after the anumation.
         var myThis = this;    

         a.onComplete.subscribe(function() {
            var srcShadow = myThis.srcShadow;

            // Hide proxy
            Dom.setStyle(proxy, "visibility", "hidden");

            // Insert and show the real dashlet
            myThis.insertSrcEl(srcEl);

            // Hide shadow
            Dom.setStyle(srcShadow, "display", "none");
         });
         a.animate();
      },

      /**
       * Checks what was dragged and to where, so it knows if to add or move the dashlet.
       *
       * @method insertSrcEl
       * @param srcEl {HTMLelement}
       */
      insertSrcEl: function CD_insertSrcEl(srcEl)
      {
         // Find out to where and from the dashlet was dragged.
         var destUl = this.srcShadow.parentNode;
         if (this.customiseDashletComponent.getColumnIndex(srcEl) === 0)
         {
            // It was an "available dashlet" that was dragged, should it be added?
            if (this.customiseDashletComponent.getColumnIndex(this.srcShadow) > 0)
            {
               // Yes, add it since it was dropped over a column.
               this.customiseDashletComponent.copyAndInsertDashlet(srcEl, destUl, this.srcShadow);
            }
         }
         else
         {
            // It was an "column dashlet" that was dragged, move it.
            destUl.insertBefore(srcEl, this.srcShadow);
            this.customiseDashletComponent.focusDashletAfterDomChange(srcEl, true);
         }
         // Show the new dashlet.
         Dom.setStyle(srcEl, "visibility", "");
         Dom.setStyle(srcEl, "display", "");
      },

      /**
       * Callback that gets called when a element was dropped over a target.
       *
       * @method onDragDrop
       * @param event {HTMLelement}
       * @param id {string} The id of the target element the proxy was dropped over.
       */
      onDragDrop: function CD_onDragDrop(event, id)
      {
         // Find the drop target and save it for later.
         var destEl = Dom.get(id);
         this.droppedOnEl = destEl;
         if (!this.customiseDashletComponent.isAddTarget(this.droppedOnEl))
         {
            // If it was
            return
         }

         /**
          * Ok, it was dropped on an add target.
          *
          * Normally we would know this if the proxy was dragged above other
          * dashlets ("li" elements) since we in that case would have placed
          * the shadow inside that column to "give space" for the new dashlet.
          *
          * However, if the column was empty OR the proxy only was dragged over
          * the columns "free space" (not over a "li" element) the shadow
          * would not have been placed inside the column. The proxy can also
          * have been dropped over the original dashlet.
          *
          * Below is the code where we check that and if that is the case, add
          * the shadow to the column so we later can decide where the dashlet
          * should be placed.
          *
          */
         if (DDM.interactionInfo.drop.length === 1)
         {
            // The position of the cursor at the time of the drop (YAHOO.util.Point)
            var pt = DDM.interactionInfo.point;

            // The region occupied by the source element at the time of the drop
            var region = YAHOO.util.Region.getRegion(this.srcShadow);

            /**
             * Check to see if we are over the source element's location.
             * We will append to the bottom of the list once we are sure it
             * was a drop in the negative space (the area of the list without any list items)
             */
            if (!region.intersect(pt))
            {
               // Add only to the list if it isn't full
               destEl = Dom.get(id);
               if (!this.customiseDashletComponent.isColumnFull(destEl))
               {
                  // Add to the list
                  destEl.appendChild(this.srcShadow);

                  // Refresh the drag n drop managers cache.
                  var destDD = DDM.getDDById(id);
                  destDD.isEmpty = false;
                  DDM.refreshCache();
               }

            }

         }
      },

      /**
       * Callback that gets called when the proxy is dragged out from a drop target.
       *
       * @method onDragOut
       * @param event {HTMLelement}
       * @param id {string} The id of the target element the proxy was dragged out of
       */
      onDragOut: function CD_onDragOut(event, id)
      {
         // Reset the droppedOn proprerty
         this.droppedOnEl = null;
         
         var prevDestEl = Dom.get(id);
         if(this.customiseDashletComponent.isOfTagType(prevDestEl, "ul"))
         {
            // Place the shadow in the dashlets original position
            this._resetSrcShadow();
         }
      },

      /**
       * Callback that gets called repeatedly when the proxy is dragged.
       * Keeps track of the direction the user is drawing the mouse so we on
       * the dragOver can decide if the shadow element should be placed above
       * or over other dashlets.
       *
       * @method onDrag
       * @param event {HTMLelement}
       */
      onDrag: function CD_onDrag(event)
      {
         // Keep track of the direction of the drag for use during onDragOver
         var y = Event.getPageY(event);
         if (y < this.lastY)
         {
            this.goingUp = true;
         }
         else if (y > this.lastY)
         {
            this.goingUp = false;
         }
         this.lastY = y;
      },

      /**
       * Callback that gets called when the proxy is over a drop target.
       * Places out the "invisible" shadow element to make space for the new
       * dashlet in the column.
       *
       * @method onDragOver
       * @param event {HTMLelement}
       * @param id {string} The id of the target element the proxy was dragged over.
       */
      onDragOver: function CD_onDragOver(event, id)
      {
         // Get the element the proxy was dragged over
         var destEl = Dom.get(id);

         // We are only concerned with list items, we ignore the dragover
         // notifications for the list since those are handled by onDragDrop().
         if (this.customiseDashletComponent.isOfTagType(destEl, "li"))
         {
            /**
             * Check what columns we dragged from and drag above and make sure
             * the dest columns isn't full.
             */
            var srcEl = this.getEl();
            var srcElColumn = this.customiseDashletComponent.getColumnIndex(srcEl);
            var destElColumn = this.customiseDashletComponent.getColumnIndex(destEl);
            if (!this.customiseDashletComponent.isColumnFull(destEl.parentNode) ||
                srcElColumn === destElColumn)
            {
               // Make sure we only add the shadow to a column and not the available dashlets
               if (destElColumn > 0)
               {
                  // Hide the original dashlet since we are about to show it as a shadow somewhere else
                  if (srcElColumn > 0)
                  {
                     Dom.setStyle(srcEl, "display", "none");
                  }

                  // Show shadow instead of original dashlet next to its target
                  Dom.setStyle(this.srcShadow, "display", "");
                  var p = destEl.parentNode;
                  if (this.goingUp)
                  {
                     // insert above
                     p.insertBefore(this.srcShadow, destEl);
                  }
                  else
                  {
                     // insert below
                     p.insertBefore(this.srcShadow, destEl.nextSibling);
                  }
                  DDM.refreshCache();
               }
            }
         }
      }
   });

})();

/* Dummy instance to load optional YUI components early */
new Alfresco.CustomiseDashlets(null);
